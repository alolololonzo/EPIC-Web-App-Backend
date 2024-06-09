package com.team32.epicwebapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * A data class for all modules. Each student can
 * have multiple registered modules.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Entity
@JsonIgnoreProperties(value = {"students"})
@Getter @Setter
@NoArgsConstructor
public class Modules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modulecode")
    private String moduleCode;

    @Column(name = "modulename")
    private String moduleName;

    @Column(name = "academicyear")
    private String academicYear;

    @Column(name = "stage")
    private Integer stage;

    @ManyToMany(mappedBy = "registeredModules")
    private Set<Users> students;

    public Modules(String moduleCode, String moduleName, String academicYear, Integer stage) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.academicYear = academicYear;
        this.stage = stage;
    }
}
