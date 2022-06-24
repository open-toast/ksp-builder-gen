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

package com.toasttab.ksp.builder.example

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserBuilderTest {
    @Test
    fun `build fails when a required property is missing`() {
        val builder = UserBuilder()
        builder.email("hi@hi.com")

        val e = assertThrows<Exception> { builder.build() }
        assertThat(e).hasMessageThat().contains("property name is not set")
    }

    @Test
    fun `build does not fail when an optional property is missing`() {
        val builder = UserBuilder()
        builder.name("hi")
        builder.addresses(listOf())
        val o = builder.build()
        assertThat(o.name).isEqualTo("hi")
        assertThat(o.active).isEqualTo(true)
    }

    @Test
    fun `happy path`() {
        val builder = UserBuilder()
        builder.name("hi")
        builder.email("hi@hi.com")
        builder.addAddresses("123 Foo St")
        builder.addAllAddresses(Iterable { sequenceOf("200 Bar St").iterator() })
        val o = builder.build()
        assertThat(o.name).isEqualTo("hi")
        assertThat(o.email).isEqualTo("hi@hi.com")
        assertThat(o.addresses).containsExactly("123 Foo St", "200 Bar St")
    }

    @Test
    fun `from`() {
        val builder = UserBuilder(User("hi", "hi@hi.com", listOf("123 Foo St")))

        builder.name("bye")

        val o = builder.build()
        assertThat(o.name).isEqualTo("bye")
        assertThat(o.email).isEqualTo("hi@hi.com")
        assertThat(o.addresses).containsExactly("123 Foo St")
    }
}