package com.team32.epicwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

/**
 * A data class providing all data for a request to
 * retrieve the data of a module.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class GetModuleRequest {

    private String moduleCode;

    private String moduleName;

    private String academicYear;

    private Integer stage;
}
