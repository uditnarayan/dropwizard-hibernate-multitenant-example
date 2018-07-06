package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAuthContext {

    @JsonProperty
    private String key;

    @JsonProperty
    private String secret;
}
