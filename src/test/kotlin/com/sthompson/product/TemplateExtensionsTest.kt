package com.sthompson.product

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TemplateExtensionsTest {

    @Test
    fun `should return correct values from extension methods`() {
        // Given
        val product = Product(
            id = 1L,
            name = "Test Product",
            description = "A product for testing",
            category = Category("Testing"),
            weight = Weight(BigDecimal("123.45")),
            unitPrice = UnitPrice(BigDecimal("99.99"))
        )

        // When & Then
        assertEquals("Testing", TemplateExtensions.categoryName(product))
        assertEquals(BigDecimal("123.45"), TemplateExtensions.weightInGrams(product))
        assertEquals(BigDecimal("99.99"), TemplateExtensions.priceAmount(product))
    }
}
