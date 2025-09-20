package com.sthompson.readingList

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class BookMapper {

    fun toDto(entity: BookEntity): Book {
        val authorsFormatted = entity.authors.joinToString(", ") { it.name }
        return Book(
            id = entity.id!!, // Assuming id is never null for a persisted entity
            title = entity.title,
            authorsFormatted = authorsFormatted
        )
    }
}
