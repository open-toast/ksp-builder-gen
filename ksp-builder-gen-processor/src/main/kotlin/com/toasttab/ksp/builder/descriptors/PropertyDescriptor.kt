/*
 * Copyright (c) 2025 Toast Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toasttab.ksp.builder.descriptors

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.toasttab.ksp.builder.annotations.GenerateBuilder

sealed interface PropertyDescriptor {
    val name: String
    val type: TypeName
    val builderType: TypeName
    val fromObjectConverter: String
    val initialValue: String
    val constructorProperty: Boolean

    companion object {
        @OptIn(KspExperimental::class)
        fun fromPropertyDeclaration(
            prop: KSPropertyDeclaration,
            hasDefault: Boolean,
        ): PropertyDescriptor {
            val name = prop.simpleName.asString()
            val type = prop.type.toTypeName()
            val constructorProperty = prop.isAnnotationPresent(GenerateBuilder.BuilderConstructorProperty::class)

            if (type is ParameterizedTypeName) {
                val containerSpec = ContainerSpec.forType(type.rawType)

                if (containerSpec != null) {
                    return ContainerPropertyDescriptor(
                        name = name,
                        type = type,
                        builderType = containerSpec.rawBuilderType.parameterizedBy(type.typeArguments).copy(nullable = type.isNullable),
                        parameters = type.typeArguments,
                        containerSpec = containerSpec,
                        constructorProperty = constructorProperty,
                    )
                }
            }

            val defaultAnnotation = prop.getAnnotationsByType(GenerateBuilder.Default::class).firstOrNull()

            return ScalarPropertyDescriptor(
                name = name,
                type = type,
                initialValue =
                    if (defaultAnnotation != null) {
                        if (hasDefault) {
                            defaultAnnotation.value
                        } else {
                            error("property $name is annotated with Default but lacks a Kotlin default")
                        }
                    } else {
                        "null"
                    },
                constructorProperty = constructorProperty,
            )
        }
    }
}

class ScalarPropertyDescriptor internal constructor(
    override val name: String,
    override val type: TypeName,
    override val initialValue: String,
    override val constructorProperty: Boolean,
) : PropertyDescriptor {
    override val builderType = if (initialValue == "null") type.copy(nullable = true) else type
    override val fromObjectConverter = ""
}

data class ContainerPropertyDescriptor internal constructor(
    override val name: String,
    override val type: TypeName,
    override val builderType: TypeName,
    val parameters: List<TypeName>,
    val containerSpec: ContainerSpec,
    override val constructorProperty: Boolean,
) : PropertyDescriptor {
    override val fromObjectConverter =
        if (type.isNullable) {
            "?.${containerSpec.converter}"
        } else {
            ".${containerSpec.converter}"
        }

    override val initialValue =
        if (type.isNullable) {
            "null"
        } else {
            containerSpec.initializer
        }

    private val capitalizedName = name.replaceFirstChar(Char::titlecase)

    val initMethodName by lazy { "init$capitalizedName" }
    val insertMethodName = "${containerSpec.insertName}$capitalizedName"
    val insertAllMethodName = "${containerSpec.insertAllName}$capitalizedName"
    val internalAccessor =
        if (builderType.isNullable) {
            "$initMethodName()"
        } else {
            name
        }
}
