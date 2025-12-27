package com.webcrafters.confease_backend.controller;

import com.webcrafters.confease_backend.model.ReviewAssignment;
import com.webcrafters.confease_backend.repository.ReviewAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review-assignments")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewAssignmentController {

    @Autowired
    private ReviewAssignmentRepository reviewAssignmentRepository;

    // Get all review assignments
    @GetMapping
    public ResponseEntity<List<ReviewAssignment>> getAllReviewAssignments() {
        List<ReviewAssignment> assignments = reviewAssignmentRepository.findAll();
        return ResponseEntity.ok(assignments);
    }

    // Get review assignment by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReviewAssignment> getReviewAssignmentById(@PathVariable Long id) {
        Optional<ReviewAssignment> assignment = reviewAssignmentRepository.findById(id);
        if (assignment.isPresent()) {
            return ResponseEntity.ok(assignment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get review assignments by reviewer ID
    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<List<ReviewAssignment>> getReviewAssignmentsByReviewer(@PathVariable Long reviewerId) {
        List<ReviewAssignment> assignments = reviewAssignmentRepository.findByReviewerId(reviewerId);
        return ResponseEntity.ok(assignments);
    }

    // Create a new review assignment
    @PostMapping
    public ResponseEntity<ReviewAssignment> createReviewAssignment(@RequestBody ReviewAssignment assignment) {
        ReviewAssignment savedAssignment = reviewAssignmentRepository.save(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAssignment);
    }

    // Update an existing review assignment
    @PutMapping("/{id}")
    public ResponseEntity<ReviewAssignment> updateReviewAssignment(@PathVariable Long id, @RequestBody ReviewAssignment assignmentDetails) {
        Optional<ReviewAssignment> optionalAssignment = reviewAssignmentRepository.findById(id);
        if (optionalAssignment.isPresent()) {
            ReviewAssignment assignment = optionalAssignment.get();
            assignment.setPaper_id(assignmentDetails.getPaper_id());
            assignment.setReviewer_id(assignmentDetails.getReviewer_id());
            assignment.setAssigned_at(assignmentDetails.getAssigned_at());
            assignment.setDue_date(assignmentDetails.getDue_date());
            assignment.setStatus(assignmentDetails.getStatus());
            ReviewAssignment updatedAssignment = reviewAssignmentRepository.save(assignment);
            return ResponseEntity.ok(updatedAssignment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a review assignment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewAssignment(@PathVariable Long id) {
        Optional<ReviewAssignment> assignment = reviewAssignmentRepository.findById(id);
        if (assignment.isPresent()) {
            reviewAssignmentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
