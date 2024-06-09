package com.team32.epicwebapp.payload.request;


import lombok.Getter;
import lombok.Setter;

/**
 * A data class providing all data for a request to
 * retrieve basic data of a user.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class GetUsersRequest {

    private String username;

    private Integer stage;

    private String moduleCode;
}
