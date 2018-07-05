package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.commands.resources;

import com.codahale.metrics.annotation.Timed;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.commands.models.Person;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Path("/{id: \\d+}")
    @GET
    @Timed
    public Response getPerson(@Valid @PathParam("id") int id) {
        Person person = Person.builder().id(1).name("Udit Naryan").email("udit@hackerearth.com").build();
        return Response.ok(person).build();
    }

}
