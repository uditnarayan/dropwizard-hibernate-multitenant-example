package io.github.uditnaryan.dropwizard.hibernate.multitenant.example.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
@Builder
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NaturalId
    @Column(name = "uuid")
    /**
     * As `key` is the keyword in mysql,
     * renaming of column is required.
     */
    private String key;

    private String secret;
}
