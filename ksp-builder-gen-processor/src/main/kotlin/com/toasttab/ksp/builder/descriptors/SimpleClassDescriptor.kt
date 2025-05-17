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
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.toasttab.ksp.builder.annotations.GenerateBuilder

class SimpleClassDescriptor private constructor(
    val packageName: String,
    val simpleName: String,
    val builderName: String,
    val properties: List<PropertyDescriptor>,
    val deprecated: Boolean,
) {
    val constructorProperties: List<PropertyDescriptor> get() = properties.filter { it.constructorProperty }
    val nonConstructorProperties: List<PropertyDescriptor> get() = properties.filter { !it.constructorProperty }

    companion object {
        @OptIn(KspExperimental::class)
        fun fromDeclaration(cls: KSClassDeclaration): SimpleClassDescriptor {
            val constructor =
                checkNotNull(cls.primaryConstructor) {
                    "primary constructor for ${cls.simpleName} is missing"
                }

            val annotation = cls.getAnnotationsByType(GenerateBuilder::class).first()

            val properties = cls.getAllProperties().associateBy { it.simpleName.asString() }

            val actualProperties =
                constructor.parameters.map {
                    if (!it.isVal && !it.isVar) {
                        error("constructor parameter ${it.name} of ${cls.simpleName}  is not backed by a property")
                    }

                    val prop =
                        checkNotNull(properties[it.name!!.asString()]) {
                            "constructor parameter ${it.name} of ${cls.simpleName} is not backed by a property"
                        }

                    if (!prop.isPublic()) {
                        error("property ${prop.simpleName} of ${cls.simpleName} is not public")
                    }

                    PropertyDescriptor.fromPropertyDeclaration(prop = prop, hasDefault = it.hasDefault)
                }

            val simpleName = cls.simpleName.asString()

            val builderName = annotation.name.ifEmpty { "${simpleName}Builder" }

            return SimpleClassDescriptor(
                packageName = cls.packageName.asString(),
                simpleName = simpleName,
                builderName = builderName,
                properties = actualProperties,
                deprecated = annotation.deprecated,
            )
        }
    }
}
