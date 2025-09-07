package com.sthompson.product

import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.HeaderParam
import java.math.BigDecimal
import java.net.URI

@Path("/products")
class ProductResource {

    @Inject
    @Location("products.qute.html")
    lateinit var productsTemplate: Template

    @Inject
    @Location("_products-list.qute.html")
    lateinit var productsListTemplate: Template

    @Inject
    @Location("add-product.qute.html")
    lateinit var addProductTemplate: Template

    @Inject
    @Location("_product-row.qute.html")
    lateinit var productRowTemplate: Template

    @Inject
    @Location("_add-product-form.qute.html")
    lateinit var addProductFormTemplate: Template

    @Inject
    lateinit var productMapper: ProductMapper

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    fun getProducts(@QueryParam("search") search: String?, @HeaderParam("HX-Request") hxRequest: String?): TemplateInstance {
        val products = if (search.isNullOrBlank()) {
            ProductEntity.listAll()
        } else {
            ProductEntity.findByNameOrDescription(search)
        }
        val domainProducts = products.map { productMapper.toDomain(it) }

        val template = if (hxRequest != null) {
            productsListTemplate
        } else {
            productsTemplate
        }
        return template.data("products", domainProducts)
    }

    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    fun addProductForm(): TemplateInstance {
        return addProductTemplate.instance()
    }

    @GET
    @Path("/add-form")
    @Produces(MediaType.TEXT_HTML)
    fun addProductFormPartial(): TemplateInstance {
        return addProductFormTemplate.instance()
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    fun addProduct(
        @FormParam("name") name: String,
        @FormParam("description") description: String,
        @FormParam("category") category: String,
        @FormParam("weight") weight: BigDecimal,
        @FormParam("unitPrice") unitPrice: BigDecimal
    ): TemplateInstance {
        val newProductEntity = ProductEntity(
            name = name,
            description = description,
            category = category,
            weight = weight,
            unitPrice = unitPrice
        )
        ProductEntity.persist(newProductEntity)
        val domainProduct = productMapper.toDomain(newProductEntity)
        return productRowTemplate.data("product", domainProduct)
    }
}
