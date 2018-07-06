package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.Response;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSnakeCase
public class ErrorResponse {

    @JsonProperty
    private int statusCode;

    @JsonProperty
    private String status;

    public static ErrorResponse fromResponseStatus(Response.Status status) {
        return new ErrorResponse(status.getStatusCode(), status.getReasonPhrase());
    }
}
