/*
 * Copyright (c) 2022 Toast Inc.
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

    companion object {
        @OptIn(KspExperimental::class)
        fun fromPropertyDeclaration(prop: KSPropertyDeclaration, hasDefault: Boolean): PropertyDescriptor {
            val name = prop.simpleName.asString()
            val type = prop.type.toTypeName()
            val defaultAnnotation = prop.getAnnotationsByType(GenerateBuilder.Default::class).firstOrNull()

            if (type is ParameterizedTypeName) {
                val custom = ContainerSpec.forType(type.rawType)

                if (custom != null) {
                    val builderType = custom.rawBuilderType.parameterizedBy(type.typeArguments).copy(nullable = type.isNullable)

                    return ContainerPropertyDescriptor(name, type, builderType, type.typeArguments, custom)
                }
            }

            val initialValue = if (defaultAnnotation != null) {
                if (hasDefault) {
                    defaultAnnotation.value
                } else {
                    error("property $name is annotated with Default but lacks a Kotlin default")
                }
            } else {
                "null"
            }

            return ScalarPropertyDescriptor(name, type, initialValue)
        }
    }
}

class ScalarPropertyDescriptor internal constructor(
    override val name: String,
    override val type: TypeName,
    override val initialValue: String
) : PropertyDescriptor {
    override val builderType = if (initialValue == "null") type.copy(nullable = true) else type
    override val fromObjectConverter = ""
}

data class ContainerPropertyDescriptor internal constructor(
    override val name: String,
    override val type: TypeName,
    override val builderType: TypeName,
    val parameters: List<TypeName>,
    val containerSpec: ContainerSpec
) : PropertyDescriptor {
    override val fromObjectConverter = if (type.isNullable) {
        "?.${containerSpec.converter}"
    } else {
        ".${containerSpec.converter}"
    }

    override val initialValue = if (type.isNullable) {
        "null"
    } else {
        containerSpec.initializer
    }

    private val capitalizedName = name.replaceFirstChar(Char::titlecase)

    val initMethodName by lazy { "init$capitalizedName" }
    val insertMethodName = "${containerSpec.insertName}$capitalizedName"
    val insertAllMethodName = "${containerSpec.insertAllName}$capitalizedName"
    val internalAccessor = if (builderType.isNullable) {
        "$initMethodName()"
    } else {
        name
    }
}
