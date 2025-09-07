package com.sthompson

import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Path("/")
class HTMXResource {

    @Inject
    @Location("index.html")
    lateinit var index: Template

    @Inject
    @Location("hello.qute.html")
    lateinit var helloTemplate: Template

    @Inject
    @Location("time.qute.html")
    lateinit var timeTemplate: Template

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun index(): TemplateInstance {
        return index.data("name", "HTMX User")
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_HTML)
    fun hello(): TemplateInstance {
        return helloTemplate.instance()
    }

    @GET
    @Path("/time")
    @Produces(MediaType.TEXT_HTML)
    fun currentTime(): TemplateInstance {
        val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return timeTemplate.data("time", currentTime)
    }
}
