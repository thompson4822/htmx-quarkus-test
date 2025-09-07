package com.sthompson.product

import com.github.javafaker.Faker
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.transaction.Transactional
import java.math.BigDecimal

@ApplicationScoped
class DataGenerator {

    @Transactional
    fun onStart(@Observes _ev: StartupEvent) {
        ProductEntity.deleteAll()

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
}
