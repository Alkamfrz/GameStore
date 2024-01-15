package com.dethrone.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "publisher_id")
@Entity
@Table(name = "publisher")
public class Publisher implements Serializable {

    @OneToMany(mappedBy = "publisher")
    private List<Game> games;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID publisher_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_name")
    private String publisher_name;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_country")
    private String publisher_country;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_city")
    private String publisher_city;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_address")
    private String publisher_address;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_phone")
    private String publisher_phone;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_email")
    private String publisher_email;

    @Null
    @Size(min = 2, max = 50)
    @Column(name = "publisher_website")
    private String publisher_website;

    @Lob
    @Null
    @Column(name = "publisher_description", columnDefinition = "LONGTEXT")
    private String publisher_description;
}
