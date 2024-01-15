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
    @Column(name = "game_release_date")
    private Date game_release_date;

    @NonNull
    @Column(name = "game_rating")
    private Double game_rating;

    @NonNull
    @Column(name = "game_price")
    private Double game_price;

    @Lob
    @Null
    @Column(name = "game_description", columnDefinition = "LONGTEXT")
    private String game_description;

    @NonNull
    @Column(name = "game_image")
    private String game_image;

    @ManyToMany
    @JoinTable(
        name = "game_genre", 
        joinColumns = @JoinColumn(name = "game_id"), 
        inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;
}