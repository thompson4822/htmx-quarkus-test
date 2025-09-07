package com.sthompson.product

import java.math.BigDecimal

@JvmInline
value class Category(val name: String)

@JvmInline
value class Weight(val grams: BigDecimal) {
    init {
        require(grams >= BigDecimal.ZERO) { "Weight cannot be negative." }
    }
}

@JvmInline
value class UnitPrice(val amount: BigDecimal) {
    init {
        require(amount >= BigDecimal.ZERO) { "Unit price cannot be negative." }
    }
}

// This is our pure, immutable domain object.
data class Product(
    val id: Long?,
    val name: String,
    val description: String,
    val category: Category,
    val weight: Weight,
    val unitPrice: UnitPrice
)
