package com.webcrafters.confease_backend.repository;

import com.webcrafters.confease_backend.model.ReviewAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewAssignmentRepository extends JpaRepository<ReviewAssignment, Long> {
    @Query("SELECT ra FROM ReviewAssignment ra WHERE ra.reviewer_id = :reviewerId")
    List<ReviewAssignment> findByReviewerId(@Param("reviewerId") Long reviewerId);
    
    @Query("SELECT ra FROM ReviewAssignment ra WHERE ra.paper_id = :paperId")
    List<ReviewAssignment> findByPaperId(@Param("paperId") Long paperId);
}
