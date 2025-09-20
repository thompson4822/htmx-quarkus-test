package com.sthompson.readingList

import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.DefaultValue
import jakarta.ws.rs.core.MediaType
import io.quarkus.panache.common.Page

@Path("/reading-list")
class ReadingListResource {

    @Inject
    @Location("readingList/readingList.html")
    lateinit var readingListTemplate: Template

    // This is the new "smart" component that handles pagination
    @Inject
    @Location("readingList/_paginated-books.qute.html")
    lateinit var paginatedBooksTemplate: Template

    @Inject
    lateinit var bookMapper: BookMapper

    private companion object {
        const val PAGE_SIZE = 10
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun getReadingList(): TemplateInstance {
        return readingListTemplate.instance()
    }

    // This endpoint now returns the full paginated books component
    @GET
    @Path("/books")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    fun getBooks(@QueryParam("page") @DefaultValue("0") page: Int): TemplateInstance {
        val booksQuery = BookEntity.findAll()
        val totalBooks = booksQuery.count()
        val books = booksQuery.page(Page.of(page, PAGE_SIZE)).list()
            .map { bookMapper.toDto(it) }

        val hasNextPage = (page + 1) * PAGE_SIZE < totalBooks
        val nextPage = if (hasNextPage) page + 1 else null

        val hasPreviousPage = page > 0
        val previousPage = if (hasPreviousPage) page - 1 else null

        return paginatedBooksTemplate.data(
            "books", books,
            "nextPage", nextPage,
            "previousPage", previousPage,
            "currentPage", page
        )
    }
}
