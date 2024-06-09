package com.team32.epicwebapp.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * A data class providing all data for a staff request to
 * make an announcement to students.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class AnnouncementRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String staffName;

    @NotBlank
    private String announcementName;


    @NotBlank
    private String content;

    private String moduleCode;

    private Integer stage;
}
