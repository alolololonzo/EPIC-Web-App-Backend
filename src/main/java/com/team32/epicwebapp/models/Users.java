package com.team32.epicwebapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A data class for all users. Each user can have multiple
 * linked {@link Modules} and {@link Role} instances.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@DynamicUpdate
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "stage")
    private Integer stage;

    @Column(name = "firstlogin")
    private Boolean firstLogin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_roles",
                joinColumns =  @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_modules", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "modules_id"))
    private Set<Modules> registeredModules;

    public Users(String name, String username, String password, Integer stage, Boolean firstLogin) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.stage = stage;
        this.firstLogin = firstLogin;
    }
}
