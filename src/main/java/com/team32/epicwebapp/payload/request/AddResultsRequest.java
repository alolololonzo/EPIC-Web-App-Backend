package com.team32.epicwebapp.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * A data class providing all data for a request to
 * add results to a user.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class AddResultsRequest {

    @NotBlank
    private final String username;

    @NotBlank
    private final String moduleCode;

    @NotBlank
    private final String quizName;

    @NotBlank
    private final Date date;

    @NotBlank
    private final int result;
}
