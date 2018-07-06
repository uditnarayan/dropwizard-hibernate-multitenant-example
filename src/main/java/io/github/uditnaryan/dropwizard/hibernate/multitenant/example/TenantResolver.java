package io.github.uditnaryan.dropwizard.hibernate.multitenant.example;

import io.dropwizard.hibernate.multitenant.ITenantResolver;
import io.dropwizard.hibernate.multitenant.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ContainerRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
public class TenantResolver implements ITenantResolver {
    private final MultiTenantConfiguration  multiTenantConfiguration;

    public TenantResolver(MultiTenantConfiguration multiTenantConfiguration) {
        this.multiTenantConfiguration = multiTenantConfiguration;
    }

    private String getTenantIdFromHeader(ContainerRequest containerRequest) {
        String tenantHeaderProperty = this.multiTenantConfiguration.getTenantHeader();
        String tenantHeaderValue = containerRequest.getHeaderString(tenantHeaderProperty);
        log.info("Tenant id: {}", tenantHeaderValue);
        return tenantHeaderValue;
    }

    public Optional<Tenant> resolve(ContainerRequest containerRequest) {
        final String tenantId = this.getTenantIdFromHeader(containerRequest);
        List<Tenant> tenants = this.multiTenantConfiguration.getTenants();
        return tenants.stream().filter(t->t.getId().equals(tenantId)).findFirst();
    }
}
