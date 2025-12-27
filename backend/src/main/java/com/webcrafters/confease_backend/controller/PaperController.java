package com.webcrafters.confease_backend.controller;

import com.webcrafters.confease_backend.model.Paper;
import com.webcrafters.confease_backend.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/papers")
@CrossOrigin(origins = "http://localhost:4200")
public class PaperController {

    @Autowired
    private PaperRepository paperRepository;

    // Get all papers
    @GetMapping
    public ResponseEntity<List<Paper>> getAllPapers() {
        List<Paper> papers = paperRepository.findAll();
        return ResponseEntity.ok(papers);
    }

    // Get paper by ID
    @GetMapping("/{id}")
    public ResponseEntity<Paper> getPaperById(@PathVariable Long id) {
        Optional<Paper> paper = paperRepository.findById(id);
        if (paper.isPresent()) {
            return ResponseEntity.ok(paper.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new paper
    @PostMapping
    public ResponseEntity<Paper> createPaper(@RequestBody Paper paper) {
        Paper savedPaper = paperRepository.save(paper);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaper);
    }

    // Update an existing paper
    @PutMapping("/{id}")
    public ResponseEntity<Paper> updatePaper(@PathVariable Long id, @RequestBody Paper paperDetails) {
        Optional<Paper> optionalPaper = paperRepository.findById(id);
        if (optionalPaper.isPresent()) {
            Paper paper = optionalPaper.get();
            // Update fields
            paper.setTrack_id(paperDetails.getTrack_id());
            paper.setTitle(paperDetails.getTitle());
            paper.setAbstractText(paperDetails.getAbstractText());
            paper.setSubmission_file(paperDetails.getSubmission_file());
            paper.setFile_type(paperDetails.getFile_type());
            paper.setVersion(paperDetails.getVersion());
            paper.setPlagiarism_score(paperDetails.getPlagiarism_score());
            paper.setStatus(paperDetails.getStatus());
            paper.setSubmitted_by(paperDetails.getSubmitted_by());
            // Note: submitted_at and last_updated are typically not updated manually, as they are timestamps

            Paper updatedPaper = paperRepository.save(paper);
            return ResponseEntity.ok(updatedPaper);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a paper
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaper(@PathVariable Long id) {
        if (paperRepository.existsById(id)) {
            paperRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get papers by user ID (author's own papers)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Paper>> getPapersByUserId(@PathVariable Long userId) {
        List<Paper> papers = paperRepository.findBySubmittedBy(userId);
        return ResponseEntity.ok(papers);
    }

    // Check if user has any accepted papers
    @GetMapping("/user/{userId}/has-accepted")
    public ResponseEntity<Boolean> userHasAcceptedPaper(@PathVariable Long userId) {
        List<Paper> papers = paperRepository.findBySubmittedBy(userId);
        boolean hasAccepted = papers.stream()
                .anyMatch(paper -> "accepted".equalsIgnoreCase(paper.getStatus()) 
                                || "reviewed".equalsIgnoreCase(paper.getStatus()));
        return ResponseEntity.ok(hasAccepted);
    }

    // Amend and resubmit a rejected paper
    @PostMapping("/{id}/amend")
    public ResponseEntity<Paper> amendPaper(@PathVariable Long id, @RequestBody Paper amendedPaper) {
        Optional<Paper> optionalPaper = paperRepository.findById(id);
        if (optionalPaper.isPresent()) {
            Paper paper = optionalPaper.get();
            
            // Check if paper is rejected
            if (!"Rejected".equalsIgnoreCase(paper.getStatus())) {
                return ResponseEntity.badRequest().build();
            }
            
            // Update paper with amended content
            paper.setTitle(amendedPaper.getTitle());
            paper.setAbstractText(amendedPaper.getAbstractText());
            paper.setTrack_id(amendedPaper.getTrack_id());
            paper.setSubmission_file(amendedPaper.getSubmission_file());
            paper.setFile_type(amendedPaper.getFile_type());
            
            // Increment version
            paper.setVersion(paper.getVersion() != null ? paper.getVersion() + 1 : 2);
            
            // Reset status to Submitted for re-review
            paper.setStatus("Submitted");
            
            // Update last_updated timestamp (handled automatically by database)
            
            Paper updatedPaper = paperRepository.save(paper);
            return ResponseEntity.ok(updatedPaper);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}