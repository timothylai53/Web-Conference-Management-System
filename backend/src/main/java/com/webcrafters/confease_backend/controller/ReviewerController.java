package com.webcrafters.confease_backend.controller;

import com.webcrafters.confease_backend.model.Reviewer;
import com.webcrafters.confease_backend.model.ReviewAssignment;
import com.webcrafters.confease_backend.model.Paper;
import com.webcrafters.confease_backend.model.User;
import com.webcrafters.confease_backend.service.ReviewerService;
import com.webcrafters.confease_backend.repository.ReviewAssignmentRepository;
import com.webcrafters.confease_backend.repository.PaperRepository;
import com.webcrafters.confease_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviewers")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewerController {

    private final ReviewerService reviewerService;
    private final ReviewAssignmentRepository reviewAssignmentRepository;
    private final PaperRepository paperRepository;
    private final UserRepository userRepository;

    public ReviewerController(ReviewerService reviewerService, 
                            ReviewAssignmentRepository reviewAssignmentRepository,
                            PaperRepository paperRepository,
                            UserRepository userRepository) {
        this.reviewerService = reviewerService;
        this.reviewAssignmentRepository = reviewAssignmentRepository;
        this.paperRepository = paperRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllReviewers() {
        List<Reviewer> reviewers = reviewerService.getAll();
        List<Map<String, Object>> result = reviewers.stream().map(reviewer -> {
            Map<String, Object> reviewerMap = new HashMap<>();
            reviewerMap.put("reviewer_id", reviewer.getReviewer_id());
            reviewerMap.put("user_id", reviewer.getUser_id());
            reviewerMap.put("expertise_area", reviewer.getExpertise_area());
            reviewerMap.put("max_papers", reviewer.getMax_papers());
            
            // Get user details
            User user = userRepository.findById(reviewer.getUser_id()).orElse(null);
            if (user != null) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("user_id", user.getUser_id());
                userMap.put("email", user.getEmail());
                userMap.put("first_name", user.getFirst_name());
                userMap.put("last_name", user.getLast_name());
                userMap.put("affiliation", user.getAffiliation());
                reviewerMap.put("user", userMap);
            }
            
            return reviewerMap;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reviewer> getReviewerById(@PathVariable Long id) {
        Reviewer reviewer = reviewerService.getById(id);
        if (reviewer != null) return ResponseEntity.ok(reviewer);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Reviewer> createReviewer(@RequestBody Reviewer reviewer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewerService.create(reviewer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reviewer> updateReviewer(@PathVariable Long id, @RequestBody Reviewer reviewerDetails) {
        Reviewer updated = reviewerService.update(id, reviewerDetails);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewer(@PathVariable Long id) {
        reviewerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reviewerId}/assignments")
    public ResponseEntity<List<Map<String, Object>>> getAssignmentsByReviewer(@PathVariable Long reviewerId) {
        List<ReviewAssignment> assignments = reviewAssignmentRepository.findByReviewerId(reviewerId);
        
        List<Map<String, Object>> result = assignments.stream().map(assignment -> {
            Map<String, Object> assignmentMap = new HashMap<>();
            assignmentMap.put("assignmentId", assignment.getAssignment_id());
            assignmentMap.put("paperId", assignment.getPaper_id());
            assignmentMap.put("reviewerId", assignment.getReviewer_id());
            assignmentMap.put("assignedAt", assignment.getAssigned_at());
            assignmentMap.put("dueDate", assignment.getDue_date());
            assignmentMap.put("status", assignment.getStatus());
            
            // Get paper title
            Paper paper = paperRepository.findById(assignment.getPaper_id()).orElse(null);
            assignmentMap.put("paperTitle", paper != null ? paper.getTitle() : "Unknown Paper");
            
            return assignmentMap;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
}
