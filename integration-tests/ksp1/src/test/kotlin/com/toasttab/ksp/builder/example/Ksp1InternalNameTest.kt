package com.toasttab.ksp.builder.example

import com.toasttab.ksp.builder.annotations.GeneratedBuilder
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.startsWith

class Ksp1InternalNameTest: AbstractInternalNameTest() {
    @Test
    fun `InternalNameBuilder is generated with ksp1`() {
        val annotations = InternalNameBuilder::class.annotations.filterIsInstance<GeneratedBuilder>()

        expectThat(annotations).hasSize(1)

        expectThat(annotations.first().forClass).isEqualTo(InternalName::class)
        expectThat(annotations.first().kspVersion).startsWith("1.")
    }
}