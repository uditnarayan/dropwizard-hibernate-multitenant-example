package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.storage;

import io.dropwizard.hibernate.AbstractDAO;
import io.github.uditnaryan.dropwizard.hibernate.multitenant.example.models.Person;
import org.hibernate.SessionFactory;

public class PersonDAO extends AbstractDAO<PersonEntity> {

    public PersonDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Person findById(int id) {
        PersonEntity entity = this.get(id);
        return entity != null
                ? this.entityToPerson(entity)
                : null;
    }

    public Person create(Person person) {
        PersonEntity entity = this.personToEntity(person);
        entity = this.persist(entity);
        return this.entityToPerson(entity);
    }

    private Person entityToPerson(PersonEntity entity) {
        return Person.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    private PersonEntity personToEntity(Person person) {
        return PersonEntity.builder()
                .id(person.getId())
                .name(person.getName())
                .email(person.getEmail())
                .build();
    }
}
