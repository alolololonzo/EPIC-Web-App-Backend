package com.team32.epicwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * A data class for all data exchanged when a user
 * registers an account, including validation for
 * field inputs from the frontend.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class RegistrationRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    private String name;

    @NotBlank
    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Integer stage;

    private boolean firstLogin;
}
