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
    @Location("products/products.html")
    lateinit var productsTemplate: Template

    @Inject
    @Location("products/_products-list.qute.html")
    lateinit var productsListTemplate: Template

    @Inject
    @Location("products/_product-row.qute.html")
    lateinit var productRowTemplate: Template

    @Inject
    @Location("products/_add-product-form.qute.html")
    lateinit var addProductFormTemplate: Template

    @Inject
    @Location("products/new-product.html")
    lateinit var newProductTemplate: Template

    @Inject
    lateinit var productMapper: ProductMapper

    /**
     * Serves the main products page or the product list fragment.
     *
     * When accessed via a standard browser request, it renders the full `products.html` page,
     * including the initial list of all products.
     * When accessed via an HTMX request, it returns only the `_products-list.qute.html` fragment,
     * which contains the filtered or complete list of products.
     *
     * HTMX Fragment Details:
     * @htmx-trigger `load` on `#product-list`, and `keyup changed delay:500ms` on the search input.
     * @htmx-target `#product-list`
     * @htmx-swap `outerHTML`
     * @htmx-indicator `#spinner`
     */
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

        val template = if (hxRequest == "true") {
            productsListTemplate
        } else {
            productsTemplate
        }
        return template.data("products", domainProducts)
    }

    /**
     * Serves the full page for adding a new product.
     */
    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    fun addNewProductPage(): TemplateInstance {
        return newProductTemplate.instance()
    }

    /**
     * HTMX Fragment: Renders the 'Add New Product' form.
     *
     * This fragment provides the HTML form for creating a new product.
     *
     * @htmx-trigger click on the 'Add New Product' button.
     * @htmx-target #add-product-form-container
     * @htmx-swap innerHTML
     */
    @GET
    @Path("/add-form")
    @Produces(MediaType.TEXT_HTML)
    fun addProductFormPartial(): TemplateInstance {
        return addProductFormTemplate.instance()
    }

    /**
     * Processes the submission of the new product form.
     *
     * For HTMX requests, this endpoint returns an Out-of-Band (OOB) swap response that
     * simultaneously appends the new product row to the table and clears the form container.
     * For standard form submissions, it redirects to the main products page.
     *
     * HTMX Fragment Details:
     * @htmx-trigger submit on the add product form.
     * @htmx-swap Out-of-Band: `beforeend` for `#product-table-body` and `innerHTML` for `#add-product-form-container`.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    fun addProduct(
        @FormParam("name") name: String,
        @FormParam("description") description: String,
        @FormParam("category") category: String,
        @FormParam("weight") weight: BigDecimal,
        @FormParam("unitPrice") unitPrice: BigDecimal,
        @HeaderParam("HX-Request") hxRequest: String?
    ): Response {
        val newProductEntity = ProductEntity(
            name = name,
            description = description,
            category = category,
            weight = weight,
            unitPrice = unitPrice
        )
        ProductEntity.persist(newProductEntity)

        if (hxRequest == "true") {
            val domainProduct = productMapper.toDomain(newProductEntity)
            val newRowContent = productRowTemplate.data("product", domainProduct).render()
            // Wrap the row in a table structure to make it a valid HTML snippet for the parser.
            // The hx-swap-oob attribute on the tbody will cause its *contents* to be swapped into the correct place.
            val newRowOob = "<table><tbody hx-swap-oob=\"beforeend:#product-table-body\">$newRowContent</tbody></table>"
            val emptyFormHtml = "<div id=\"add-product-form-container\" hx-swap-oob=\"true\"></div>"
            return Response.ok(newRowOob + emptyFormHtml, MediaType.TEXT_HTML).build()
        }

        return Response.seeOther(URI.create("/products")).build()
    }
}