package com.team32.epicwebapp.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A data class for creating a JWT response
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private long id;
    private String name;
    private String username;
    private Integer stage;
    private Boolean firstLogin;
    private List<String> roles;

    public JwtResponse(String accessToken, long id, String username, String name, Integer stage, Boolean firstLogin, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.name = name;
        this.stage = stage;
        this.firstLogin = firstLogin;
        this.roles = roles;
    }
}
