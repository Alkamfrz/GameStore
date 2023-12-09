/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author alkam
 */
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
    @Size(min = 2, max = 50)
    @Column(name = "price")
    private Integer price;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "description")
    private String description;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "image")
    private String image;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "genre_id")
    private UUID genre_id;
    
    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "platform_id")
    private UUID platform_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "publisher_id")
    private UUID publisher_id;
}
