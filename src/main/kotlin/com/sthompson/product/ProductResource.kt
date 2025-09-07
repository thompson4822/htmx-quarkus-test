package com.sthompson.product

import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/products")
class ProductResource {

    @Inject
    @Location("products.qute.html")
    lateinit var productsTemplate: Template

    @Inject
    lateinit var productMapper: ProductMapper

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    fun getProducts(): TemplateInstance {
        val products = ProductEntity.listAll().map { productMapper.toDomain(it) }
        return productsTemplate.data("products", products)
    }
}
