package com.team32.epicwebapp.controllers;


import com.team32.epicwebapp.models.Announcements;
import com.team32.epicwebapp.models.Modules;
import com.team32.epicwebapp.models.Result;
import com.team32.epicwebapp.models.Users;
import com.team32.epicwebapp.payload.request.*;
import com.team32.epicwebapp.payload.response.MessageResponse;
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
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The controller for all mapped requests made by users
 * logged in with the staff role.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    AnnouncementsRepository ar;

    @Autowired
    UsersRepository ur;

    @Autowired
    ModulesRepository mr;

    @Autowired
    JsonUserData jsonUserData;

    @Autowired
    JsonStorage jsonStorage;


    //Add Module
    @PostMapping("/module/addModule")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addModule(@Valid @RequestBody ModuleAddRequest addRequest) {
        String moduleCode = addRequest.getModuleCode() + " " + addRequest.getAcademicYear();
        Optional<Modules> existingModule = mr.findByModuleCode(moduleCode);
        if(existingModule.isPresent()) {
            return ResponseEntity.ok(new MessageResponse("This module " + moduleCode + " already exists."));
        }else {

            Modules module = new Modules(moduleCode, addRequest.getModuleName(), addRequest.getAcademicYear(), addRequest.getStage());
            mr.save(module);

            return ResponseEntity.ok(new MessageResponse("Module " + moduleCode + " added."));
        }

    }


    //Add Announcement
    @PostMapping("/announcements/add")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addAnnouncement(@AuthenticationPrincipal MyUserDetails user, @Valid @RequestBody AnnouncementRequest announcementRequest) {
        String staffName = user.getName();
        LocalDate todayDate = LocalDate.now(ZoneId.of("Europe/London"));
        LocalTime todayTime = LocalTime.now(ZoneId.of("Europe/London"));
        Date date = Date.valueOf(todayDate);
        Time time = Time.valueOf(todayTime);

        Announcements announcement = new Announcements(staffName, announcementRequest.getAnnouncementName()
                , date, time, announcementRequest.getContent(), announcementRequest.getModuleCode()
                , announcementRequest.getStage());
        ar.save(announcement);

        return ResponseEntity.ok(new MessageResponse("Announcement added."));
    }


    //Get current user's posted announcements
    @GetMapping("/announcements/posted")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getPostedAnnouncements(@AuthenticationPrincipal MyUserDetails user){
        String staffName = user.getName();
        List<Announcements> announcements = ar.findByStaffName(staffName);

        List<Announcements> sortedAnnouncements = new ArrayList<>(Announcements.orderDateTime(announcements));

        return ResponseEntity.ok().body(sortedAnnouncements);
    }


    //Get list of students by stage
    @PostMapping("/getStudentsByStage")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getStudentsByStage(@Valid @RequestBody GetUsersRequest getRequest) {

        Integer stage = getRequest.getStage();
        List<Users> students = ur.findByStage(stage);

        return ResponseEntity.ok(students);


    }


    //Add group students into a module by stage
    @PostMapping("/module/addStudentsByStage")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addStudentsByStage(@Valid @RequestBody AddStudentsRequest addRequest) {

        Integer stage = addRequest.getStage();
        List<Users> students = ur.findByStage(stage);
        String message;

        if (students.isEmpty()) {
            message = "There are no students in this stage.";
        } else {
            Optional<Modules> optional = mr.findByModuleCode(addRequest.getModuleCode());
            if (optional.isPresent()) {
                Modules module = optional.get();
                for (Users user : students) {
                    Set<Modules> studentModules = user.getRegisteredModules();
                    studentModules.add(module);
                    user.setRegisteredModules(studentModules);
                    ur.save(user);
                }
                message = students.size() + " student(s) registered to module " + addRequest.getModuleCode();
            }else{
                message = "Module " + addRequest.getModuleCode() + " does not exist.";
            }
        }
        return ResponseEntity.ok(new MessageResponse(message));
    }


    //get all modules
    @GetMapping("/modules/all")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<Modules>> getAllModules() {
        List<Modules> modules = StreamSupport.stream(mr.findAll().spliterator(), false).collect(Collectors.toList());
        return ResponseEntity.ok(modules);
    }


    //get modules by year
    @PostMapping("/modules/byYear")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getModulesByYear(GetModuleRequest getModule) {
        List<Modules> modules = mr.findByAcademicYear(getModule.getAcademicYear());
        return ResponseEntity.ok(modules);
    }


    //get modules by stage
    @PostMapping("/modules/byStage")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getModulesByStage(GetModuleRequest getModule) {
        List<Modules> modules = mr.findByStage(getModule.getStage());
            return ResponseEntity.ok(modules);
    }


    //add student into module by username
    @PostMapping("/module/addStudentsByUsername")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addStudentsByUsername(@Valid @RequestBody AddStudentsRequest addRequest) {
        Optional<Users> user = ur.findByUsername(addRequest.getUsername());
        String response;
        Optional<Modules> optional = mr.findByModuleCode(addRequest.getModuleCode());

        if (user.isPresent()) {
            if(optional.isPresent()){
            Users student = user.get();
            Modules module = optional.get();
            Set<Modules> modules = student.getRegisteredModules();
            modules.add(module);
            student.setRegisteredModules(modules);
            ur.save(student);

            response = student.getUsername() + " registered to module " + addRequest.getModuleCode();
        } else {
            response = "Module does not exist. Please try again.";
        }
        }else{
            response = "User does not exist. Please try again.";
        }
        return ResponseEntity.ok(new MessageResponse(response));
    }


    //get list of students by module
    @PostMapping("/modules/getStudentsByModule")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getStudentsByModule(@Valid @RequestBody GetModuleRequest moduleRequest) {
        String moduleCode = moduleRequest.getModuleCode();
        Optional<Modules> optional = mr.findByModuleCode(moduleCode);
        List<Users> students = new ArrayList<>();

        if(optional.isPresent()) {
            Modules module = optional.get();
            students.addAll(module.getStudents());
                return ResponseEntity.ok(students);
        }

        return ResponseEntity.ok(students);

    }


    //get list of module results
    @PostMapping("/results/average")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getModuleAverage(@Valid @RequestBody GetModuleRequest getRequest) {
        String moduleCode = getRequest.getModuleCode();
        List<Result> results = new ArrayList<>();
        Optional<Modules> optional = mr.findByModuleCode(moduleCode);

        if(optional.isPresent()) {
            Modules module = optional.get();
            Set<Users> studentSet = module.getStudents();
            for (Users student : studentSet) {
                long id = student.getId();
                JsonUserData userData = this.jsonStorage.getUserData(id);
                Set<Result> moduleResults = userData.getByModule(module.getId());
                results.addAll(moduleResults);
                results.sort(new Result.SortByDate());
            }
        }

        return ResponseEntity.ok(results);
    }


    //get stage results of a student
    @PostMapping("/results/student/stage")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getStudentStageResults(@Valid @RequestBody GetUsersRequest usersRequest) {
        Optional<Users> optional = ur.findByUsername(usersRequest.getUsername());
        Integer stage = usersRequest.getStage();
        long userId = 0;

        if (optional.isPresent()) {
            userId = optional.get().getId();
        }

        JsonUserData userData = this.jsonStorage.getUserData(userId);
        Set<Result> moduleResults = userData.getByStage(stage);
        List<Result> results = new ArrayList<>(moduleResults);

        return ResponseEntity.ok(Result.order(Result.order(results)));
    }


    //add results for a student
    @PostMapping("/modules/results/add")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addStudentResults(@Valid @RequestBody AddResultsRequest addRequest) {
        String username = addRequest.getUsername();
        Optional<Modules> optional = mr.findByModuleCode(addRequest.getModuleCode());
        Optional<Users> user = ur.findByUsername(username);
        String message;

        if (user.isPresent()) {
            if(optional.isPresent()) {
                long userId = user.get().getId();
                Modules module = optional.get();
                JsonUserData userData = this.jsonStorage.getUserData(userId);
                userData.insertResult(new Result(module.getId(), addRequest.getQuizName(), addRequest.getDate(), module.getStage(), addRequest.getResult()));
                this.jsonStorage.writeUserData(userId);
                message = addRequest.getQuizName() + " result has been added for student " + username;

            }else{
                message = "This module does not exist. Please try again";
            }

        } else {

            message = "User " +username+ " does not exist. Please try again";

        }

        return ResponseEntity.ok(new MessageResponse(message));

    }


    //get student's module result
    @PostMapping("/results/student/module")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getStudentModuleResults(@Valid @RequestBody GetUsersRequest usersRequest) {
        Optional<Users> user = ur.findByUsername(usersRequest.getUsername());
        Optional<Modules> module = mr.findByModuleCode(usersRequest.getModuleCode());
        long userId;
        List<Result> results = new ArrayList<>();

        if (user.isPresent() && module.isPresent()) {

            userId = user.get().getId();
            JsonUserData userData = this.jsonStorage.getUserData(userId);
            Set<Result> moduleResults = userData.getByModule(module.get().getId());
            results.addAll(moduleResults);
            results.sort(new Result.SortByDate());
        }

        return ResponseEntity.ok(results);
    }


    //get a list of current modules by student's stage
    @PostMapping("/findStudentModules")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> findStudentById(@Valid @RequestBody GetUsersRequest usersRequest){
        Optional<Users> user = ur.findByUsername(usersRequest.getUsername());
        List<Modules> modules = new ArrayList<>();

        if(user.isPresent()){
            Users student = user.get();
            Set<Modules> allModules = student.getRegisteredModules();
            for(Modules module:allModules){
                if(module.getStage().equals(student.getStage())){
                    modules.add(module);
                }
            }
        }
        return ResponseEntity.ok(modules);

    }

}
