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

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import java.util.Collections

abstract class AbstractUserDirectoryBuilderTest {
    @Test
    fun `build`() {
        val directory =
            DirectoryBuilder("org")
                .putAllUsers(mapOf("1" to User("Foo", null), "2" to User("Bar", null)))
                .putUsers("3", User("Zzz", null))
                .build()

        expectThat(directory.organization).isEqualTo("org")
        expectThat(directory.users.mapValues { it.value.name }.toList())
            .containsExactly("1" to "Foo", "2" to "Bar", "3" to "Zzz")
    }

    @Test
    fun `allow not setting not nullable collection`() {
        val directory = assertDoesNotThrow { DirectoryBuilder("org").build() }
        expectThat(directory.users).isEmpty()
    }

    @Test
    fun `verify collections are defensively copied`() {
        val builder = DirectoryBuilder(UserDirectory("org", Collections.emptyMap(), Collections.emptyMap()))
        val directory =
            assertDoesNotThrow {
                builder.putUsers("1", User("Foo", null)).build()
            }
        expectThat(directory.users.mapValues { it.value.name }.toList()).containsExactly("1" to "Foo")
    }
}
