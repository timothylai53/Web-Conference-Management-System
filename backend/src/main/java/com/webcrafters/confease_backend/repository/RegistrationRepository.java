package com.webcrafters.confease_backend.repository;

import com.webcrafters.confease_backend.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    @Query("SELECT r FROM Registration r WHERE r.user_id = :userId")
    List<Registration> findByUser_id(@Param("userId") Long userId);
}
