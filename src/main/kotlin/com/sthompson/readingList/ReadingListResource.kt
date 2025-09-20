package com.sthompson.readingList

import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/reading-list")
class ReadingListResource {

    @Inject
    @Location("readingList/readingList.html")
    lateinit var readingListTemplate: Template

    // HTMX fragment for getting a list of books
    @Inject
    @Location("readingList/_books-list.qute.html")
    lateinit var booksListTemplate: Template

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun getReadingList(): TemplateInstance {
        return readingListTemplate.instance()
    }

    // HTMX fragment for getting a list of books
    @GET
    @Path("/books")
    @Produces(MediaType.TEXT_HTML)
    fun getBooks(): TemplateInstance {
        val books = BookEntity.listAll()
        return booksListTemplate.data("books", books)
    }
}
