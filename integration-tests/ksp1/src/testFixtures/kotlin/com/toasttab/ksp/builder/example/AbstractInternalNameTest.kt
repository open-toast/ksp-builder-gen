package com.toasttab.ksp.builder.example

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

abstract class AbstractInternalNameTest {
    @Test
    fun `InternalNameBuilder is internal`() {
        expectThat(InternalNameBuilder::class.visibility).isEqualTo(kotlin.reflect.KVisibility.INTERNAL)
    }
}