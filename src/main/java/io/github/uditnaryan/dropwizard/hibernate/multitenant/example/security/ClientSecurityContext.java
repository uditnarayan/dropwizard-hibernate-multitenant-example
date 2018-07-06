package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.security;

import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.Client;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class ClientSecurityContext implements SecurityContext {

    private final Client principal;

    private final SecurityContext securityContext;

    public ClientSecurityContext(Client principal, SecurityContext securityContext) {
        this.principal = principal;
        this.securityContext = securityContext;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return role.equals("CLIENT");
    }

    @Override
    public boolean isSecure() {
        return securityContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
