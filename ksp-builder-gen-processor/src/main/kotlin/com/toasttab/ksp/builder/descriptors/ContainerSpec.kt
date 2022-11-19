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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asClassName

class ContainerSpec(
    val initializer: String,
    val converter: String,
    val rawBuilderType: ClassName,
    val insertAllName: String,
    val insertAllType: ClassName,
    val insertName: String,
    val insertParams: List<String>
) {
    companion object {
        private const val COLLECTIONS_PKG = "kotlin.collections"
        private fun collectionSpec(initializer: String, converter: String, rawBuilderType: ClassName) = ContainerSpec(
            initializer = initializer,
            converter = converter,
            rawBuilderType = rawBuilderType,
            insertAllName = "addAll",
            insertAllType = ClassName(COLLECTIONS_PKG, "Iterable"),
            insertName = "add",
            insertParams = listOf("o")
        )
        private val LIST_SPEC = collectionSpec("mutableListOf()", "toMutableList()", ClassName(COLLECTIONS_PKG, "MutableList"))

        private val BUILTINS = mapOf(
            Map::class.asClassName() to ContainerSpec(
                initializer = "mutableMapOf()",
                converter = "toMutableMap()",
                rawBuilderType = ClassName(COLLECTIONS_PKG, "MutableMap"),
                insertAllType = ClassName(COLLECTIONS_PKG, "Map"),
                insertAllName = "putAll",
                insertName = "put",
                insertParams = listOf("k", "v")
            ),
            Collection::class.asClassName() to LIST_SPEC,
            List::class.asClassName() to LIST_SPEC,
            Set::class.asClassName() to collectionSpec(
                initializer = "mutableSetOf()",
                converter = "toMutableSet()",
                rawBuilderType = ClassName(COLLECTIONS_PKG, "MutableSet")
            )
        )

        fun forType(type: ClassName) = BUILTINS[type]
    }
}
