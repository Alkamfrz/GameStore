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
@EqualsAndHashCode(of = "genre_id")
@Entity
@Table(name = "genre")
public class Genre implements Serializable {

    @ManyToMany(mappedBy = "genres")
    private Set<Game> games;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID genre_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "genre_name")
    private String genre_name;

    @Lob
    @Null
    @Column(name = "genre_description", columnDefinition = "LONGTEXT")
    private String genre_description;
}
