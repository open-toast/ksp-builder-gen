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

package com.toasttab.ksp.builder.example

import com.toasttab.ksp.builder.annotations.GeneratedBuilder
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.startsWith

class Ksp2InternalNameTest: AbstractInternalNameTest() {
    @Test
    fun `InternalNameBuilder is generated with ksp2`() {
        val annotations = InternalNameBuilder::class.annotations.filterIsInstance<GeneratedBuilder>()

        expectThat(annotations).hasSize(1)

        expectThat(annotations.first().forClass).isEqualTo(InternalName::class)
        expectThat(annotations.first().kspVersion).startsWith("2.")
    }
}
