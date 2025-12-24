package com.webcrafters.confease_backend.repository;

import com.webcrafters.confease_backend.model.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    @Query("SELECT p FROM Paper p WHERE p.submitted_by = :userId")
    List<Paper> findBySubmittedBy(@Param("userId") Long userId);
}
