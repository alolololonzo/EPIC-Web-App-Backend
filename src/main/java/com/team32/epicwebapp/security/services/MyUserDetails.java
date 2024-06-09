package com.team32.epicwebapp.security.services;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team32.epicwebapp.models.Modules;
import com.team32.epicwebapp.models.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * A data class to create a UserDetails object
 * for the current logged in user.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class MyUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private long id;

    private String username;

    @JsonIgnore
    private String password;

    private String name;

    private Integer stage;

    private Set<Modules> registeredModules;

    private Boolean firstLogin;

    private Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(long id, String username, String name, String password, Integer stage, Boolean firstLogin,
                         Set<Modules> registeredModules, Collection<? extends GrantedAuthority> authorities){
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.stage = stage;
        this.registeredModules = registeredModules;
        this.firstLogin = firstLogin;
        this.authorities = authorities;
    }

    public static MyUserDetails build(Users user){
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new MyUserDetails(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                user.getStage(),
                user.getFirstLogin(),
                user.getRegisteredModules(),
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MyUserDetails user = (MyUserDetails) o;
        return Objects.equals(id, user.id);
    }
}
