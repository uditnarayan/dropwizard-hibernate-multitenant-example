package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.security;

import com.google.common.base.Strings;
import io.dropwizard.auth.AuthFilter;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.Client;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.ClientAuthContext;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.StringTokenizer;

@PreMatching
@Slf4j
@Priority(Priorities.AUTHENTICATION)
public class ClientAuthFilter extends AuthFilter<ClientAuthContext, Client> {

    private final ClientAuthenticator clientAuthenticator;

    public ClientAuthFilter(ClientAuthenticator clientAuthenticator) {
        this.clientAuthenticator = clientAuthenticator;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Optional<Client> optionalClient = null;
        try {
            ClientAuthContext authContext = getCredentials(containerRequestContext);
            optionalClient = clientAuthenticator.authenticate(authContext);
            if (optionalClient.isPresent()) {
                SecurityContext securityContext = new ClientSecurityContext(
                        optionalClient.get(), containerRequestContext.getSecurityContext());
                containerRequestContext.setSecurityContext(securityContext);
            }
            else {
                throw new Exception("Invalid client secret provided.");
            }
        }
        catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.UNAUTHORIZED);
        }
    }

    private ClientAuthContext getCredentials(ContainerRequestContext containerRequestContext) throws Exception {
        String authHeader = containerRequestContext.getHeaderString("Authorization");

        if (Strings.isNullOrEmpty(authHeader)) {
            throw new WebApplicationException("Authorization header missing.");
        }

        StringTokenizer st = new StringTokenizer(authHeader);
        if (st.hasMoreTokens()) {
            String basic = st.nextToken();

            if (basic.equalsIgnoreCase("basic")) {
                String credentials = new String(Base64.getDecoder().decode(st.nextToken()));
                log.debug(credentials);
                int p = credentials.indexOf(":");
                if (p != -1) {
                    String clientKey = credentials.substring(0, p).trim();
                    String clientSecret = credentials.substring(p + 1).trim();
                    return new ClientAuthContext(clientKey, clientSecret);
                } else {
                    throw new WebApplicationException("Invalid authorization header value.", Response.Status.UNAUTHORIZED);
                }
            }
            else {
                throw new WebApplicationException("Invalid authorization header value.", Response.Status.UNAUTHORIZED);
            }
        }
        else {
            throw new WebApplicationException("Invalid authorization header value.", Response.Status.UNAUTHORIZED);
        }
    }
}
