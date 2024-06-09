package com.team32.epicwebapp.controllers;

import com.team32.epicwebapp.models.Announcements;
import com.team32.epicwebapp.models.Modules;
import com.team32.epicwebapp.models.Result;
import com.team32.epicwebapp.payload.request.GetModuleRequest;
import com.team32.epicwebapp.payload.request.GetUsersRequest;
import com.team32.epicwebapp.repositories.AnnouncementsRepository;
import com.team32.epicwebapp.repositories.ModulesRepository;
import com.team32.epicwebapp.repositories.UsersRepository;
import com.team32.epicwebapp.security.services.MyUserDetails;
import com.team32.epicwebapp.storage.JsonStorage;
import com.team32.epicwebapp.storage.JsonUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The controller for all mapped requests made by users
 * logged in with the student role.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    AnnouncementsRepository ar;

    @Autowired
    ModulesRepository mr;

    @Autowired
    UsersRepository ur;

    @Autowired
    JsonStorage jsonStorage;


    //get list of announcements
    @GetMapping("/announcements")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Announcements>> getAnnouncements(@AuthenticationPrincipal MyUserDetails user){
        Integer stage = user.getStage();
        Set<Modules> modules = user.getRegisteredModules();
        List<Announcements> stageAnnouncements = ar.findByStage(stage);
        List<Announcements> allAnnouncements = ar.findByStage(null);
        List<Announcements> generalAnnouncements = new ArrayList<>();

        for(Announcements announcement:allAnnouncements){
            if(announcement.getModuleCode() == null){
                generalAnnouncements.add(announcement);
            }
        }
        List<String> moduleCodes = new ArrayList<>();

        for(Modules module:modules){
            moduleCodes.add(module.getModuleCode());
        }

        List<Announcements> announcements = Stream.concat(stageAnnouncements.stream(), generalAnnouncements.stream())
                                            .collect(Collectors.toList());

        for(String moduleCode : moduleCodes){
            List<Announcements> moduleAnnouncements = ar.findByModuleCode(moduleCode);
            announcements.addAll(moduleAnnouncements);
        }

        List<Announcements> sortedAnnouncements = new ArrayList<>(Announcements.orderDateTime(announcements));

        return ResponseEntity.ok().body(sortedAnnouncements);
    }


    //get modules
    @GetMapping("/modules")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getModules(@AuthenticationPrincipal MyUserDetails user){
        List<Modules> modules = new ArrayList<>(user.getRegisteredModules());
        return ResponseEntity.ok(modules);

    }


    //get modules by stage
    @PostMapping("/modules/byStage")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getModulesByStage(@AuthenticationPrincipal MyUserDetails user, @Valid @RequestBody GetModuleRequest getRequest){
        List<Modules> allModules = new ArrayList<>(user.getRegisteredModules());
        List<Modules> modules = new ArrayList<>();

        for(Modules module:allModules){
            if(module.getStage().equals(getRequest.getStage())){
                modules.add(module);
            }
        }
        return ResponseEntity.ok(modules);
    }


    //get results by module
    @PostMapping("/resultsByModule")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getModuleResults(@AuthenticationPrincipal MyUserDetails user, @Valid @RequestBody GetModuleRequest moduleRequest){
        long userId = user.getId();
        Optional<Modules> module = mr.findByModuleCode(moduleRequest.getModuleCode());
        List<Result> results = new ArrayList<>();

        if(module.isPresent()) {
            JsonUserData userData = this.jsonStorage.getUserData(userId);
            Set<Result> moduleResults = userData.getByModule(module.get().getId());
            results.addAll(moduleResults);
            results.sort(new Result.SortByDate());
        }

        return ResponseEntity.ok(results);


    }


    //get stage results
    @PostMapping("/getStageResults")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getStageResults(@AuthenticationPrincipal MyUserDetails user, @Valid @RequestBody GetUsersRequest getRequest){
        long userId = user.getId();
        Integer stage = getRequest.getStage();

        JsonUserData userData = this.jsonStorage.getUserData(userId);
        Set<Result> moduleResults = userData.getByStage(stage);
        List<Result> results = new ArrayList<>(moduleResults);

        return ResponseEntity.ok(Result.order(results));

    }


    //get all results
    @GetMapping("/getAllResults")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getAllResults(@AuthenticationPrincipal MyUserDetails user){
        long userId = user.getId();
        JsonUserData userData = this.jsonStorage.getUserData(userId);
        Set<Result> allResults = userData.getResults();
        List<Result> results = new ArrayList<>(allResults);


        return ResponseEntity.ok(Result.order(results));
    }


}
