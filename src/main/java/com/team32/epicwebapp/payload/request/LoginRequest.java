package com.team32.epicwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * A data class providing all data for a request to
 * log a user into their account via a username and
 * a password.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
