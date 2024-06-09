package com.team32.epicwebapp.repositories;

import com.team32.epicwebapp.models.Modules;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * A data class to execute CRUD methods with the
 * Modules table in the database.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
public interface ModulesRepository extends CrudRepository<Modules, Long> {

    Optional<Modules> findByModuleCode(String moduleCode);

    List<Modules> findByAcademicYear(String acadYear);

    List<Modules> findByStage(Integer stage);
}
