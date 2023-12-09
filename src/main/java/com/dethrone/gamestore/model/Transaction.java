/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dethrone.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author alkam
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "transaction_id")
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transaction_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "user_id")
    private UUID user_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "game_id")
    private UUID game_id;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "transaction_date")
    private LocalDateTime transaction_date;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "transaction_status")
    private String transaction_status;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "transaction_total")
    private Integer transaction_total;

    @NonNull
    @Size(min = 2, max = 50)
    @Column(name = "transaction_quantity")
    private Integer transaction_quantity;
}
