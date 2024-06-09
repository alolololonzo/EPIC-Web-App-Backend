package com.team32.epicwebapp.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A data class for all announcements made by staff members,
 * storing their data and linked modules and other data.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Announcements {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "staffname")
    private String staffName;

    @Column(name = "announcementname")
    private String announcementName;

    @Column(name = "announcementdate")
    private Date announcementDate;

    @Column(name = "announcementtime")
    private Time announcementTime;

    @Column(name = "content")
    private String content;

    @Column(name = "modulecode")
    private String moduleCode;

    @Column(name = "stage")
    private Integer stage;

    public Announcements(String staffName, String announcementName, Date date, Time time, String content,
                         String moduleCode, Integer stage) {
        this.staffName = staffName;
        this.announcementName = announcementName;
        this.announcementDate = date;
        this.announcementTime = time;
        this.content = content;
        this.moduleCode = moduleCode;
        this.stage = stage;
    }

    public static List<Announcements> orderDateTime(List<Announcements> announcementsList) {

        Comparator<Announcements> compareModuleDate = Comparator.comparing(Announcements::getAnnouncementDate)
                .thenComparing(Announcements::getAnnouncementTime);

        return announcementsList.stream().sorted(compareModuleDate)
                .collect(Collectors.toList());
    }
}
