package com.team32.epicwebapp.repositories;

import com.team32.epicwebapp.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A data class to execute CRUD methods with the
 * Users table in the database.
 *
 * @author Alonzo Fong
 * @version 1.0
 */

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

    List<Users> findByStage(Integer stage);
}

