package com.sthompson.product

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.config.RedirectConfig.redirectConfig
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import jakarta.transaction.Transactional
import java.math.BigDecimal

@QuarkusTest
class ProductResourceIT {

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
    fun `should return all products on initial load`() {
        given()
            .`when`().get("/products")
            .then()
            .statusCode(200)
            .body(containsString("Small Silk Gloves"))
            .body(containsString("Gorgeous Bronze Bag"))
            .body(containsString("<!DOCTYPE html>")) // Should be the full page
    }

    @Test
    fun `should return filtered products on search`() {
        given()
            .queryParam("search", "Gloves")
            .`when`().get("/products")
            .then()
            .statusCode(200)
            .body(containsString("Small Silk Gloves"))
            .body(not(containsString("Gorgeous Bronze Bag")))
            .body(containsString("<!DOCTYPE html>")) // Should be the full page
    }

    @Test
    fun `should return partial html for htmx search request`() {
        given()
            .queryParam("search", "Bag")
            .header("HX-Request", "true")
            .`when`().get("/products")
            .then()
            .statusCode(200)
            .body(not(containsString("<!DOCTYPE html>"))) // Should be a partial
            .body(containsString("Gorgeous Bronze Bag"))
            .body(not(containsString("Small Silk Gloves")))
            .body(containsString("<div id=\"product-list\">"))
    }

    @Test
    fun `should return add product page`() {
        given()
            .`when`().get("/products/new")
            .then()
            .statusCode(200)
            .body(containsString("<h1>Add a New Product</h1>"))
    }

    @Test
    fun `should return add product form partial`() {
        given()
            .`when`().get("/products/add-form")
            .then()
            .statusCode(200)
            .body(containsString("<form hx-post=\"/products\""))
    }

    @Test
    fun `should add a new product and redirect for standard post`() {
        given().config(RestAssured.config().redirect(redirectConfig().followRedirects(false)))
            .contentType(ContentType.URLENC)
            .formParam("name", "New Gadget")
            .formParam("description", "A shiny new gadget.")
            .formParam("category", "Electronics")
            .formParam("weight", "150.5")
            .formParam("unitPrice", "99.99")
            .`when`().post("/products")
            .then()
            .statusCode(303) // See Other for redirect
            .header("Location", containsString("/products"))
    }

    @Test
    fun `should add a new product and return partial for htmx post`() {
        given()
            .contentType(ContentType.URLENC)
            .header("HX-Request", "true")
            .formParam("name", "HTMX Gadget")
            .formParam("description", "An even shinier gadget.")
            .formParam("category", "Gadgets")
            .formParam("weight", "120.0")
            .formParam("unitPrice", "149.99")
            .`when`().post("/products")
            .then()
            .statusCode(200)
            .body(containsString("<tbody hx-swap-oob=\"beforeend:#product-table-body\">"))
            .body(containsString("<td>HTMX Gadget</td>"))
            .body(containsString("<div id=\"add-product-form-container\" hx-swap-oob=\"true\"></div>"))
            .body(not(containsString("<!DOCTYPE html>")))
    }
}
