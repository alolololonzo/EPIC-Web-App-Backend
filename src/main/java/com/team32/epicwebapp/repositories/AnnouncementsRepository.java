package com.team32.epicwebapp.repositories;

import com.team32.epicwebapp.models.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * A data class to execute CRUD methods with the
 * Announcements table in the database.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
public interface AnnouncementsRepository extends JpaRepository<Announcements, Long> {

    Optional<Announcements> findById(Long announcementId);

    List<Announcements> findByStage(Integer stage);

    List<Announcements> findByModuleCode(String moduleCode);

    List<Announcements> findByStaffName(String staffName);
}
