package com.sthompson.product

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import jakarta.transaction.Transactional
import java.math.BigDecimal

@QuarkusTest
class ProductEntityIT {

    @BeforeEach
    @Transactional
    fun setup() {
        ProductEntity.deleteAll()
        ProductEntity.persist(
            ProductEntity("Small Silk Gloves", "Illum facere necessitatibus quis ut tempora eum.", "Clothing", BigDecimal("928.14"), BigDecimal("658.15")),
            ProductEntity("Gorgeous Bronze Bag", "Nemo animi voluptates ut et eaque.", "Health", BigDecimal("198.77"), BigDecimal("176.68"))
        )
    }

    @Test
    @Transactional
    fun `should find products by name or description`() {
        // Search by name
        var found = ProductEntity.findByNameOrDescription("Gloves")
        assertEquals(1, found.size)
        assertEquals("Small Silk Gloves", found.first().name)

        // Search by description
        found = ProductEntity.findByNameOrDescription("voluptates")
        assertEquals(1, found.size)
        assertEquals("Gorgeous Bronze Bag", found.first().name)

        // Search with no results
        found = ProductEntity.findByNameOrDescription("nonexistent")
        assertEquals(0, found.size)
    }
}
