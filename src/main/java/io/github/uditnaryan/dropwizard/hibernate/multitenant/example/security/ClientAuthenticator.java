package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.security;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.hibernate.multitenant.contexts.TenantRequestContext;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.Client;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.ClientAuthContext;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.storage.ClientDAO;

import java.util.Optional;

public class ClientAuthenticator implements Authenticator<ClientAuthContext, Client> {

    @Override
    @UnitOfWork
    public Optional<Client> authenticate(ClientAuthContext clientAuthContext) throws AuthenticationException {
        ClientDAO clientDAO = new ClientDAO(TenantRequestContext.HIBERNATE_SESSION_FACTORY.get());
        try {
            Client client = clientDAO.findByKey(clientAuthContext.getKey());
            if (client == null) {
                throw new Exception("Invalid client key provided.");
            }
            if (client.getSecret().equals(clientAuthContext.getSecret())) {
                return Optional.of(client);
            }
            return Optional.empty();
        }
        catch (Exception e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
    }
}
