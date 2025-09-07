package com.sthompson.product

import java.math.BigDecimal

@JvmInline
value class Category(val name: String)

@JvmInline
value class Weight(val grams: BigDecimal)

@JvmInline
value class UnitPrice(val amount: BigDecimal)

// This is our pure, immutable domain object.
data class Product(
    val id: Long?,
    val name: String,
    val description: String,
    val category: Category,
    val weight: Weight,
    val unitPrice: UnitPrice
)
