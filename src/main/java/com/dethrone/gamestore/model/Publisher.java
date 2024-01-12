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

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Game> games;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID publisher_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_name")
    private String publisher_name;
}
