package com.webcrafters.confease_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviewer_application")
public class ReviewerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long application_id;
    
    private String first_name;
    private String last_name;
    private String email;
    private String password_hash;
    private String affiliation;
    private String country;
    private String orcid;
    private String expertise_area;
    private Integer max_papers = 5;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.pending;
    
    private LocalDateTime submitted_at;
    private LocalDateTime reviewed_at;
    private Long reviewed_by;
    
    @Column(columnDefinition = "TEXT")
    private String rejection_reason;

    public enum ApplicationStatus {
        pending, approved, rejected
    }

    // Constructors
    public ReviewerApplication() {
        this.submitted_at = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getApplication_id() {
        return application_id;
    }

    public void setApplication_id(Long application_id) {
        this.application_id = application_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrcid() {
        return orcid;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    public String getExpertise_area() {
        return expertise_area;
    }

    public void setExpertise_area(String expertise_area) {
        this.expertise_area = expertise_area;
    }

    public Integer getMax_papers() {
        return max_papers;
    }

    public void setMax_papers(Integer max_papers) {
        this.max_papers = max_papers;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmitted_at() {
        return submitted_at;
    }

    public void setSubmitted_at(LocalDateTime submitted_at) {
        this.submitted_at = submitted_at;
    }

    public LocalDateTime getReviewed_at() {
        return reviewed_at;
    }

    public void setReviewed_at(LocalDateTime reviewed_at) {
        this.reviewed_at = reviewed_at;
    }

    public Long getReviewed_by() {
        return reviewed_by;
    }

    public void setReviewed_by(Long reviewed_by) {
        this.reviewed_by = reviewed_by;
    }

    public String getRejection_reason() {
        return rejection_reason;
    }

    public void setRejection_reason(String rejection_reason) {
        this.rejection_reason = rejection_reason;
    }
}
