package com.team32.epicwebapp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A data class for creating a message response.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {

    private String message;
}
