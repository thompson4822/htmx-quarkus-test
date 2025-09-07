package com.sthompson.product

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "Product") // Explicitly name the table to avoid conflicts
class ProductEntity(
    var name: String,
    var description: String,
    var category: String,
    var weight: BigDecimal,
    var unitPrice: BigDecimal
) : PanacheEntity() {

    /**
     * A no-arg constructor is required for JPA.
     */
    constructor() : this("", "", "", BigDecimal.ZERO, BigDecimal.ZERO)

    companion object : PanacheCompanion<ProductEntity> {
        fun findByCategory(category: String) = list("category", category)

        fun findByNameOrDescription(query: String) = 
            list("lower(name) like ?1 or lower(description) like ?1", "%${query.lowercase()}%" )
    }
}
