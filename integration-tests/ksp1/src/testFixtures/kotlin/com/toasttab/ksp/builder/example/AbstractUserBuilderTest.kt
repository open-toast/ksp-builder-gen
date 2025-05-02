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
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.any
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.message

abstract class AbstractUserBuilderTest {
    @Test
    fun `build fails when a required property is missing`() {
        val builder = UserBuilder()
        builder.email("hi@hi.com")

        expectThrows<Exception> { builder.build() }.and {
            message.isNotNull().contains("property name is not set")
        }
    }

    @Test
    fun `build does not fail when an optional property is missing`() {
        val builder = UserBuilder()
        builder.name("hi")
        builder.addresses(listOf())
        val o = builder.build()
        expectThat(o.name).isEqualTo("hi")
        expectThat(o.active).isEqualTo(true)
    }

    @Test
    fun `happy path`() {
        val builder = UserBuilder()
        builder.name("hi")
        builder.email("hi@hi.com")
        builder.addAddresses("123 Foo St")
        builder.addAllAddresses(Iterable { sequenceOf("200 Bar St").iterator() })
        val o = builder.build()
        expectThat(o.name).isEqualTo("hi")
        expectThat(o.email).isEqualTo("hi@hi.com")
        expectThat(o.addresses).containsExactly("123 Foo St", "200 Bar St")
    }

    @Test
    fun `from`() {
        val builder = UserBuilder(User("hi", "hi@hi.com", listOf("123 Foo St")))

        builder.name("bye")

        val o = builder.build()
        expectThat(o.name).isEqualTo("bye")
        expectThat(o.email).isEqualTo("hi@hi.com")
        expectThat(o.addresses).containsExactly("123 Foo St")
    }

    @Test
    fun `UserBuilder is deprecated`() {
        expectThat(UserBuilder::class.annotations).any {
            isA<Deprecated>()
        }
    }
}
