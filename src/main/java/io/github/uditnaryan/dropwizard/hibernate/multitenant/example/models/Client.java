package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.security.Principal;

@Builder
@Data
public class Client implements Principal {

    @JsonProperty
    private int id;

    @JsonProperty
    private String key;

    @JsonProperty
    private String secret;

    public String getName() {
        return this.key;
    }
}
