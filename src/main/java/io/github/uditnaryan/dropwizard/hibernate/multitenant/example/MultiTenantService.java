package io.github.uditnaryan.dropwizard.hibernate.multitenant.example;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.hibernate.multitenant.ITenantResolver;
import io.dropwizard.hibernate.multitenant.MultiTenantHibernateBundle;
import io.dropwizard.hibernate.multitenant.Tenant;
import io.dropwizard.hibernate.multitenant.UnitOfWorkAwareMultiTenantProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.Client;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.resources.PersonResource;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.security.ClientAuthFilter;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.security.ClientAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import java.util.List;

@Slf4j
public class MultiTenantService extends Application<MultiTenantConfiguration> {

    private MultiTenantHibernateBundle<MultiTenantConfiguration> hibernateBundle;

    @Override
    public void initialize(Bootstrap<MultiTenantConfiguration> bootstrap) {
        super.initialize(bootstrap);

        String[] entityPackages = {
                "io.github.uditnaryan.dropwizard.hibernate.multitenant.example.storage"
        };

        this.hibernateBundle = new MultiTenantHibernateBundle<MultiTenantConfiguration>(entityPackages) {
            public List<Tenant> getTenants(MultiTenantConfiguration multiTenantConfiguration) {
                return multiTenantConfiguration.getTenants();
            }

            public ITenantResolver getTenantResolver(final MultiTenantConfiguration multiTenantConfiguration) {
                return new TenantResolver(multiTenantConfiguration);
            }
        };
        bootstrap.addBundle(hibernateBundle);
    }

    public void run(MultiTenantConfiguration configuration, Environment environment) throws Exception {
        System.out.println("Running application...");
        environment.jersey().register(new PersonResource());
        setupAuthFilter(environment);
    }

    public static void main(String... args) throws Exception {
        new MultiTenantService().run(args);
    }

    private void setupAuthFilter(Environment environment) {
        ClientAuthenticator clientAuthenticator = new UnitOfWorkAwareMultiTenantProxyFactory(
                this.hibernateBundle.getTenantSessionFactories())
                .create(ClientAuthenticator.class);
        ClientAuthFilter customAuthFilter = new ClientAuthFilter(clientAuthenticator);
        environment.jersey().register(new AuthDynamicFeature(customAuthFilter));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Client.class));
    }
}
