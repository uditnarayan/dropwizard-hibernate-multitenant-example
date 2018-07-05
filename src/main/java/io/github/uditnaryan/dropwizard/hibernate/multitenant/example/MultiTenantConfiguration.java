package io.github.uditnaryan.dropwizard.hibernate.multitenant.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.hibernate.multitenant.Tenant;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MultiTenantConfiguration extends Configuration {

    @NotNull
    @NotEmpty
    @JsonProperty
    private String name;

    @NotNull
    @NotEmpty
    @JsonProperty
    private String tenantHeader;

    @NotNull
    @NotEmpty
    @JsonProperty
    private List<Tenant> tenants;
}
