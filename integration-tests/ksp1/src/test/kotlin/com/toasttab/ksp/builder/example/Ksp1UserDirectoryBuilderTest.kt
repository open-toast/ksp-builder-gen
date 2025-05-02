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

import com.google.common.truth.Truth.assertThat
import com.toasttab.ksp.builder.annotations.GeneratedBuilder
import org.junit.jupiter.api.Test

class Ksp1UserDirectoryBuilderTest : AbstractUserDirectoryBuilderTest() {
    @Test
    fun `DirectoryBuilder is generated with ksp1`() {
        val annotations = DirectoryBuilder::class.annotations.filterIsInstance<GeneratedBuilder>()

        assertThat(annotations).hasSize(1)

        assertThat(annotations.first().forClass).isEqualTo(UserDirectory::class)
        assertThat(annotations.first().kspVersion).startsWith("1.")
    }
}
