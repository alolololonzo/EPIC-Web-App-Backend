package com.team32.epicwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * A data class providing all data for a request to
 * add students to the system.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class AddStudentsRequest {

    private String username;

    private Integer stage;

    @NotBlank
    private String moduleCode;
}
