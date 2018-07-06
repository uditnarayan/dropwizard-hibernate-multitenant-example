package io.github.uditnaryan.dropwizard.hibernate.multitenant.example;

import io.dropwizard.Application;
import io.dropwizard.hibernate.multitenant.ITenantResolver;
import io.dropwizard.hibernate.multitenant.MultiTenantHibernateBundle;
import io.dropwizard.hibernate.multitenant.Tenant;
import io.dropwizard.hibernate.multitenant.exceptions.MissingTenantException;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.resources.PersonResource;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ContainerRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MultiTenantService extends Application<MultiTenantConfiguration> {

    private MultiTenantHibernateBundle<MultiTenantConfiguration> hibernateBundle;

    @Override
    public void initialize(Bootstrap<MultiTenantConfiguration> bootstrap) {
        super.initialize(bootstrap);

        String[] entityPackages = {
                "io.github.uditnaryan.dropwizard.hibernate.multitenant.example.storage"
        };

        MultiTenantHibernateBundle<MultiTenantConfiguration> hibernateBundle =
                new MultiTenantHibernateBundle<MultiTenantConfiguration>(entityPackages) {
            public List<Tenant> getTenants(MultiTenantConfiguration multiTenantConfiguration) {
                return multiTenantConfiguration.getTenants();
            }

            public ITenantResolver getTenantResolver(final MultiTenantConfiguration multiTenantConfiguration) {

                return new ITenantResolver() {

                    private String getTenantIdFromHeader(ContainerRequest containerRequest) {
                        String tenantHeaderProperty = multiTenantConfiguration.getTenantHeader();
                        String tenantHeaderValue = containerRequest.getHeaderString(tenantHeaderProperty);
                        log.info("Tenant id: {}", tenantHeaderValue);
                        return tenantHeaderValue;
                    }

                    public Tenant resolve(ContainerRequest containerRequest) throws MissingTenantException {
                        final String tenantId = this.getTenantIdFromHeader(containerRequest);
                        List<Tenant> tenants = multiTenantConfiguration.getTenants();
                        Optional<Tenant> optional = tenants.stream().filter(t->t.getId().equals(tenantId)).findFirst();
                        if (optional.isPresent()) {
                            return optional.get();
                        }
                        throw new MissingTenantException("Invalid tenant provided in header: " + tenantId);
                    }
                };
            }
        };
        bootstrap.addBundle(hibernateBundle);
    }

    public void run(MultiTenantConfiguration configuration, Environment environment) throws Exception {
        System.out.println("Running application...");
        environment.jersey().register(new PersonResource());
    }

    public static void main(String... args) throws Exception {
        new MultiTenantService().run(args);
    }

}
