package com.sthompson.product

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProductMapper {

    fun toDomain(entity: ProductEntity): Product {
        return Product(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            category = Category(entity.category),
            weight = Weight(entity.weight),
            unitPrice = UnitPrice(entity.unitPrice)
        )
    }

    fun toEntity(domain: Product): ProductEntity {
        return ProductEntity(
            name = domain.name,
            description = domain.description,
            category = domain.category.name,
            weight = domain.weight.grams,
            unitPrice = domain.unitPrice.amount
        ).apply {
            id = domain.id
        }
    }
}
