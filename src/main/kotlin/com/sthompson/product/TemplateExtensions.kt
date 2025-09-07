package com.sthompson.product

import io.quarkus.qute.TemplateExtension

@TemplateExtension
object TemplateExtensions {

    @JvmStatic
    fun categoryName(product: Product): String {
        return product.category.name
    }

    @JvmStatic
    fun weightInGrams(product: Product): java.math.BigDecimal {
        return product.weight.grams
    }

    @JvmStatic
    fun priceAmount(product: Product): java.math.BigDecimal {
        return product.unitPrice.amount
    }
}
