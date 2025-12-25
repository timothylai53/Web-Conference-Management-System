package com.webcrafters.confease_backend.repository;

import com.webcrafters.confease_backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.assignment_id IN :assignmentIds")
    List<Review> findByAssignmentIdIn(@Param("assignmentIds") List<Long> assignmentIds);
}
