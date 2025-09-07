package com.sthompson.product

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductMapperTest {

    private val productMapper = ProductMapper()

    @Test
    fun `should map entity to domain`() {
        // Given
        val entity = ProductEntity(
            name = "Laptop",
            description = "A powerful laptop",
            category = "Electronics",
            weight = BigDecimal(1500),
            unitPrice = BigDecimal("1200.50")
        ).apply { id = 1L }

        // When
        val domain = productMapper.toDomain(entity)

        // Then
        assertEquals(1L, domain.id)
        assertEquals("Laptop", domain.name)
        assertEquals("A powerful laptop", domain.description)
        assertEquals(Category("Electronics"), domain.category)
        assertEquals(Weight(BigDecimal(1500)), domain.weight)
        assertEquals(UnitPrice(BigDecimal("1200.50")), domain.unitPrice)
    }

    @Test
    fun `should map domain to entity`() {
        // Given
        val domain = Product(
            id = 1L,
            name = "Mouse",
            description = "An ergonomic mouse",
            category = Category("Accessories"),
            weight = Weight(BigDecimal(250)),
            unitPrice = UnitPrice(BigDecimal("75.99"))
        )

        // When
        val entity = productMapper.toEntity(domain)

        // Then
        assertEquals(1L, entity.id)
        assertEquals("Mouse", entity.name)
        assertEquals("An ergonomic mouse", entity.description)
        assertEquals("Accessories", entity.category)
        assertEquals(BigDecimal(250), entity.weight)
        assertEquals(BigDecimal("75.99"), entity.unitPrice)
    }
}
