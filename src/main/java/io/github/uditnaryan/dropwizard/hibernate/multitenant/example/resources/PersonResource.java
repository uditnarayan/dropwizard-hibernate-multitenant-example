package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.hibernate.multitenant.contexts.TenantRequestContext;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.ErrorResponse;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.Person;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.storage.PersonDAO;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PersonResource {

    @Path("/{id: \\d+}")
    @GET
    @Timed
    @UnitOfWork
    public Response getPerson(@Valid @PathParam("id") int id) {
        try {
            PersonDAO personDAO = new PersonDAO(TenantRequestContext.HIBERNATE_SESSION_FACTORY.get());
            Person person = personDAO.findById(id);
            if (person == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(ErrorResponse.fromResponseStatus(Response.Status.NOT_FOUND))
                        .build();
            }
            return Response.ok(person).build();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.fromResponseStatus(Response.Status.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @POST
    @Timed
    @UnitOfWork
    public Response createPerson(@Valid Person person) {
        try {
            PersonDAO personDAO = new PersonDAO(TenantRequestContext.HIBERNATE_SESSION_FACTORY.get());
            Person newPerson = personDAO.create(person);
            return Response.ok(newPerson).build();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.fromResponseStatus(Response.Status.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }
}
