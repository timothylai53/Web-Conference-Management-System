package com.webcrafters.confease_backend.repository;

import com.webcrafters.confease_backend.model.ReviewerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewerApplicationRepository extends JpaRepository<ReviewerApplication, Long> {
    Optional<ReviewerApplication> findByEmail(String email);
    List<ReviewerApplication> findByStatus(ReviewerApplication.ApplicationStatus status);
    
    @Query("SELECT r FROM ReviewerApplication r WHERE r.status = :status ORDER BY r.submitted_at DESC")
    List<ReviewerApplication> findByStatusOrderedBySubmittedAt(@Param("status") ReviewerApplication.ApplicationStatus status);
}
