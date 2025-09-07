package com.sthompson.product

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class ProductValueClassTest {

    @Test
    fun `should throw exception for negative weight`() {
        // Given
        val negativeWeight = BigDecimal("-100")

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            Weight(negativeWeight)
        }
        assertEquals("Weight cannot be negative.", exception.message)
    }

    @Test
    fun `should throw exception for negative unit price`() {
        // Given
        val negativePrice = BigDecimal("-50.00")

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            UnitPrice(negativePrice)
        }
        assertEquals("Unit price cannot be negative.", exception.message)
    }

    @Test
    fun `should allow zero weight and price`() {
        // Given
        val zeroWeight = BigDecimal.ZERO
        val zeroPrice = BigDecimal.ZERO

        // When & Then
        Weight(zeroWeight)
        UnitPrice(zeroPrice)
        // No exception should be thrown
    }
}
