package com.team32.epicwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * A data class for all data exchanged when a user
 * makes a password change request, including their
 * old password for validation.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class PasswordChangeRequest {

    private String oldPassword;

    @NotBlank
    private String newPassword;
}
