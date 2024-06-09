package com.team32.epicwebapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * An entity varient of {@link ERole} providing an ID
 * for each role available to users.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Entity
@Table
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column
    private ERole name;
}
