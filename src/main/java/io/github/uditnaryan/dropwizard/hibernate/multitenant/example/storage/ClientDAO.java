package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.storage;

import io.dropwizard.hibernate.AbstractDAO;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.Client;
import org.hibernate.SessionFactory;

public class ClientDAO extends AbstractDAO<ClientEntity> {

    public ClientDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Client findByKey(String key) {
        ClientEntity entity = this.currentSession()
                .byNaturalId(this.getEntityClass())
                .using("key", key)
                .load();

        return entity != null
                ? this.entityToClient(entity)
                : null;
    }

    private Client entityToClient(ClientEntity entity) {
        return Client.builder()
                .id(entity.getId())
                .key(entity.getKey())
                .secret(entity.getSecret())
                .build();
    }

}
