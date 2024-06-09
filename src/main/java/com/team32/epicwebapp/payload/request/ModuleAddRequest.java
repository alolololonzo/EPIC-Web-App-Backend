package com.team32.epicwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * A data class providing all data for a request to
 * add a module to the system.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class ModuleAddRequest {

    @NotBlank
    private String moduleCode;

    @NotBlank
    private String moduleName;

    @NotBlank
    private String academicYear;

    @NotBlank
    private Integer stage;
}
