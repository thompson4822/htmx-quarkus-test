package com.sthompson.readingList

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.*

@Entity
@Table(name = "Book")
class BookEntity(
    var title: String,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "Book_Author",
        joinColumns = [JoinColumn(name = "book_id")],
        inverseJoinColumns = [JoinColumn(name = "author_id")]
    )
    var authors: MutableSet<AuthorEntity> = mutableSetOf()
) : PanacheEntity() {

    /**
     * A no-arg constructor is required for JPA.
     */
    constructor() : this("")

    companion object : PanacheCompanion<BookEntity> {
        fun findByTitle(title: String) = find("title", title).firstResult()
    }
}
