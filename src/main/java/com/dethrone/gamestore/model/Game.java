package com.dethrone.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "game_id")
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_id")
    private UUID game_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "game_name")
    private String game_name;

    @NonNull
    @Column(name = "price")
    private Integer price;

    @NonNull
    @Size(min = 2, max = 500)
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;
}