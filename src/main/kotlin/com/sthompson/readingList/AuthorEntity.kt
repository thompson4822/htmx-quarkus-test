package com.sthompson.readingList

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.Transient

@Entity
@Table(name = "Author")
class AuthorEntity(
    var firstName: String,
    var lastName: String,

    @ManyToMany(mappedBy = "authors")
    var books: MutableSet<BookEntity> = mutableSetOf()
) : PanacheEntity() {

    @get:Transient
    val name: String
        get() = "$firstName $lastName"

    /**
     * A no-arg constructor is required for JPA.
     */
    constructor() : this("", "")

    companion object : PanacheCompanion<AuthorEntity> {
        fun findByName(firstName: String, lastName: String) = 
            find("firstName = ?1 and lastName = ?2", firstName, lastName).firstResult()
    }
}
