package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
