package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.commands;

import io.dropwizard.Application;
import io.dropwizard.hibernate.multitenant.MultiTenantHibernateBundle;
import io.dropwizard.hibernate.multitenant.Tenant;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

public class MultiTenantService extends Application<MultiTenantConfiguration> {

    @Override
    public void initialize(Bootstrap<MultiTenantConfiguration> bootstrap) {
        super.initialize(bootstrap);

        String[] entityPackages = {
                "io.github.uditnaryan.dropwizard.hibernate.multitenant.example.commands.entities"
        };

        MultiTenantHibernateBundle<MultiTenantConfiguration> hibernateBundle =
                new MultiTenantHibernateBundle<MultiTenantConfiguration>(entityPackages) {
            public List<Tenant> getTenants(MultiTenantConfiguration multiTenantConfiguration) {
                return multiTenantConfiguration.getTenants();
            }

            public String getTenantHeaderPropertyName(MultiTenantConfiguration multiTenantConfiguration) {
                return multiTenantConfiguration.getTenantHeader();
            }
        };
    }

    public void run(MultiTenantConfiguration configuration, Environment environment) throws Exception {
        System.out.println("Hello");
    }

    public static void main(String... args) throws Exception {
        new MultiTenantService().run(args);
    }

}
