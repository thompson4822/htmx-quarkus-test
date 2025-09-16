package com.sthompson.udemy

import io.quarkus.qute.Location

import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/udemy")
class UdemyResource(@param:Location("udemy/udemy.html") private val udemy: Template) {

    @Location("udemy/_learn-more.qute.html")
    lateinit var learnMoreTemplate: Template

 

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun get(): TemplateInstance {
        return udemy.instance()
    }
    
    @GET
    @Path("/learn-more")
    @Produces(MediaType.TEXT_HTML)
    fun learnMore(): TemplateInstance {
        return learnMoreTemplate.instance()
    }
}
