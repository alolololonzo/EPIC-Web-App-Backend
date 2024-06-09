package com.team32.epicwebapp.repositories;

import com.team32.epicwebapp.models.ERole;
import com.team32.epicwebapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A data class to execute CRUD methods with the
 * Roles table in the database.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
