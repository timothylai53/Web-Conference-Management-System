package com.webcrafters.confease_backend.controller;

import com.webcrafters.confease_backend.model.*;
import com.webcrafters.confease_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/reviewer-applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewerApplicationController {

    @Autowired
    private ReviewerApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    // Submit reviewer application
    @PostMapping("/submit")
    public ResponseEntity<?> submitApplication(@RequestBody ReviewerApplication application) {
        try {
            // Check if email already exists in users or applications
            if (userRepository.findByEmail(application.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Email already registered as a user"));
            }

            Optional<ReviewerApplication> existingApp = applicationRepository.findByEmail(application.getEmail());
            if (existingApp.isPresent()) {
                ReviewerApplication existing = existingApp.get();
                if (existing.getStatus() == ReviewerApplication.ApplicationStatus.pending) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("message", "You already have a pending application"));
                } else if (existing.getStatus() == ReviewerApplication.ApplicationStatus.approved) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("message", "Your application has already been approved"));
                }
            }

            application.setSubmitted_at(LocalDateTime.now());
            application.setStatus(ReviewerApplication.ApplicationStatus.pending);
            
            ReviewerApplication saved = applicationRepository.save(application);
            return ResponseEntity.ok(Map.of(
                "message", "Application submitted successfully! Awaiting admin approval.",
                "application_id", saved.getApplication_id()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error submitting application: " + e.getMessage()));
        }
    }

    // Get all pending applications (Admin only)
    @GetMapping("/pending")
    public ResponseEntity<List<ReviewerApplication>> getPendingApplications() {
        List<ReviewerApplication> pending = applicationRepository.findByStatusOrderedBySubmittedAt(
            ReviewerApplication.ApplicationStatus.pending
        );
        return ResponseEntity.ok(pending);
    }

    // Get all applications (Admin only)
    @GetMapping("/all")
    public ResponseEntity<List<ReviewerApplication>> getAllApplications() {
        return ResponseEntity.ok(applicationRepository.findAll());
    }

    // Approve application
    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveApplication(
            @PathVariable Long id,
            @RequestParam Long adminUserId) {
        try {
            Optional<ReviewerApplication> appOpt = applicationRepository.findById(id);
            if (!appOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Application not found"));
            }

            ReviewerApplication app = appOpt.get();
            
            if (app.getStatus() != ReviewerApplication.ApplicationStatus.pending) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Application already processed"));
            }

            // Create User account
            User newUser = new User();
            newUser.setFirst_name(app.getFirst_name());
            newUser.setLast_name(app.getLast_name());
            newUser.setEmail(app.getEmail());
            newUser.setPassword_hash(app.getPassword_hash());
            newUser.setAffiliation(app.getAffiliation());
            newUser.setCountry(app.getCountry());
            newUser.setOrcid(app.getOrcid());
            newUser.setCategory("academic");
            newUser.setIs_email_verified(true);
            
            User savedUser = userRepository.save(newUser);

            // Assign Reviewer role
            Optional<Role> reviewerRoleOpt = roleRepository.findAll().stream()
                .filter(r -> r.getRole_name().equals("Reviewer"))
                .findFirst();
            
            if (reviewerRoleOpt.isPresent()) {
                UserRole userRole = new UserRole();
                userRole.setUser_id(savedUser.getUser_id());
                userRole.setRole_id(reviewerRoleOpt.get().getRole_id());
                userRole.setAssigned_at(new java.sql.Timestamp(System.currentTimeMillis()));
                userRoleRepository.save(userRole);
            }

            // Create Reviewer profile
            Reviewer reviewer = new Reviewer();
            reviewer.setUser_id(savedUser.getUser_id());
            reviewer.setExpertise_area(app.getExpertise_area());
            reviewer.setMax_papers(app.getMax_papers());
            reviewerRepository.save(reviewer);

            // Update application status
            app.setStatus(ReviewerApplication.ApplicationStatus.approved);
            app.setReviewed_at(LocalDateTime.now());
            app.setReviewed_by(adminUserId);
            applicationRepository.save(app);

            return ResponseEntity.ok(Map.of(
                "message", "Application approved! User account created.",
                "user_id", savedUser.getUser_id()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error approving application: " + e.getMessage()));
        }
    }

    // Reject application
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectApplication(
            @PathVariable Long id,
            @RequestParam Long adminUserId,
            @RequestBody Map<String, String> body) {
        try {
            Optional<ReviewerApplication> appOpt = applicationRepository.findById(id);
            if (!appOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Application not found"));
            }

            ReviewerApplication app = appOpt.get();
            
            if (app.getStatus() != ReviewerApplication.ApplicationStatus.pending) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Application already processed"));
            }

            app.setStatus(ReviewerApplication.ApplicationStatus.rejected);
            app.setReviewed_at(LocalDateTime.now());
            app.setReviewed_by(adminUserId);
            app.setRejection_reason(body.get("reason"));
            applicationRepository.save(app);

            return ResponseEntity.ok(Map.of("message", "Application rejected"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error rejecting application: " + e.getMessage()));
        }
    }
}
