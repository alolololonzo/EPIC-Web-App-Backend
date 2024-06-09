package com.team32.epicwebapp.controllers;

import com.team32.epicwebapp.models.*;
import com.team32.epicwebapp.payload.request.GetUsersRequest;
import com.team32.epicwebapp.payload.request.LoginRequest;
import com.team32.epicwebapp.payload.request.PasswordChangeRequest;
import com.team32.epicwebapp.payload.request.RegistrationRequest;
import com.team32.epicwebapp.payload.response.JwtResponse;
import com.team32.epicwebapp.payload.response.MessageResponse;
import com.team32.epicwebapp.repositories.AnnouncementsRepository;
import com.team32.epicwebapp.repositories.RoleRepository;
import com.team32.epicwebapp.repositories.UsersRepository;
import com.team32.epicwebapp.security.jwt.JwtUtils;
import com.team32.epicwebapp.security.services.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The controller for all mapped authentication requests
 * made by all users that log in.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepository ur;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AnnouncementsRepository ar;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    //Create User
    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest signUpRequest) {

        //Check if username exists
        if (ur.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is invalid"));
        }else {

            // Create new user's account
            Users user = new Users(signUpRequest.getName(), signUpRequest.getUsername(),
                    encoder.encode(signUpRequest.getPassword()), signUpRequest.getStage(), true);

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(studentRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "staff":
                            Role staffRole = roleRepository.findByName(ERole.ROLE_STAFF)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(staffRole);

                            break;
                        case "student":
                            Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(studentRole);
                    }
                });
            }
            user.setRoles(roles);
            ur.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
    }


    //Delete Users
    @PostMapping("/deleteUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody GetUsersRequest getRequest) {
        String username = getRequest.getUsername();
        String response;

        if(username.equals("admin")){
            response = "Cannot delete admin.";

        }else {
            Optional<Users> optional = ur.findByUsername(username);

            if (optional.isPresent()) {
                ur.delete(optional.get());
                response = "User deleted.";
            } else {
                response = "This user does not exist. Please try again.";
            }
        }

            return ResponseEntity.ok(response);
    }


    //Log In
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getName(),
                userDetails.getStage(),
                userDetails.getFirstLogin(),
                roles));
    }


    //Change Password
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal MyUserDetails userDetails, @Valid @RequestBody PasswordChangeRequest changeRequest) {
        String response;
        Optional<Users> optional = ur.findByUsername(userDetails.getUsername());

        if (encoder.matches(changeRequest.getOldPassword(), userDetails.getPassword())) {
            if (optional.isPresent()) {
                Users user = optional.get();
                user.setPassword(encoder.encode(changeRequest.getNewPassword()));
                ur.save(user);
                response = "Password changed.";
            } else {
                response = "An unexpected error occurred. Please try again.";
            }

        } else {
            response = "Your current password is incorrect.";
        }

        return ResponseEntity.ok(new MessageResponse(response));
    }


    //Check if it is user's first log in
    @PostMapping("/firstLogin")
    public ResponseEntity<?> firstLogin(@AuthenticationPrincipal MyUserDetails userDetails, @Valid @RequestBody PasswordChangeRequest changeRequest) {
        String response;
        Users user;
        Optional<Users> optional = ur.findByUsername(userDetails.getUsername());

        if (optional.isPresent()) {
            user = optional.get();
            user.setPassword(encoder.encode(changeRequest.getNewPassword()));
            user.setFirstLogin(false);
            ur.save(user);
            response = "Password changed.";
        } else {
            response = "An unexpected error occurred. Please try again.";
        }
        return ResponseEntity.ok(new MessageResponse(response));

    }
}
