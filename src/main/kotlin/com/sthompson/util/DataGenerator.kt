package com.sthompson.util

import com.github.javafaker.Faker
import com.sthompson.product.ProductEntity
import com.sthompson.readingList.AuthorEntity
import com.sthompson.readingList.BookEntity
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.transaction.Transactional
import java.math.BigDecimal

@ApplicationScoped
class DataGenerator {

    @Transactional
    fun onStart(@Observes _ev: StartupEvent) {
        // Clear existing data in the correct order to respect foreign key constraints
        // The Book_Author join table will be cleared automatically when books are deleted.
        BookEntity.deleteAll()
        AuthorEntity.deleteAll()
        ProductEntity.deleteAll()

        generateProducts()
        generateBooksAndAuthors()
    }

    private fun generateProducts() {
        val faker = Faker()
        val products = (1..20).map {
            ProductEntity(
                name = faker.commerce().productName(),
                description = faker.lorem().sentence(),
                category = faker.commerce().department(),
                weight = BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)),
                unitPrice = BigDecimal(faker.commerce().price(0.0, 1000.0))
            )
        }
        ProductEntity.persist(products)
    }

    private fun generateBooksAndAuthors() {
        val faker = Faker()

        // Create and persist authors first
        val authors = (1..10).map {
            AuthorEntity(
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName()
            )
        }
        AuthorEntity.persist(authors)

        // Create books and associate them with the persisted authors
        val books = (1..30).map {
            val book = BookEntity(
                title = faker.book().title()
            )
            // Assign 1 to 3 random authors to each book
            book.authors = authors.shuffled().take(faker.number().numberBetween(1, 4)).toMutableSet()
            book
        }
        BookEntity.persist(books)
    }
}
