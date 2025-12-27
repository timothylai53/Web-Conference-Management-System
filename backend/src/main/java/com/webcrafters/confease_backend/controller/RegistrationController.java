package com.webcrafters.confease_backend.controller;

import com.webcrafters.confease_backend.model.Registration;
import com.webcrafters.confease_backend.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    // Get all registrations
    @GetMapping
    public ResponseEntity<List<Registration>> getAllRegistrations() {
        List<Registration> registrations = registrationRepository.findAll();
        return ResponseEntity.ok(registrations);
    }

    // Get registration by ID
    @GetMapping("/{id}")
    public ResponseEntity<Registration> getRegistrationById(@PathVariable Long id) {
        Optional<Registration> registration = registrationRepository.findById(id);
        if (registration.isPresent()) {
            return ResponseEntity.ok(registration.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get registrations by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Registration>> getRegistrationsByUserId(@PathVariable Long userId) {
        List<Registration> registrations = registrationRepository.findByUser_id(userId);
        return ResponseEntity.ok(registrations);
    }

    // Create a new registration
    @PostMapping
    public ResponseEntity<Registration> createRegistration(@RequestBody Registration registration) {
        Registration savedRegistration = registrationRepository.save(registration);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRegistration);
    }

    // Update an existing registration
    @PutMapping("/{id}")
    public ResponseEntity<Registration> updateRegistration(@PathVariable Long id, @RequestBody Registration registrationDetails) {
        Optional<Registration> optionalRegistration = registrationRepository.findById(id);
        if (optionalRegistration.isPresent()) {
            Registration registration = optionalRegistration.get();
            // Update only non-null fields to allow partial updates
            if (registrationDetails.getUser_id() != null) {
                registration.setUser_id(registrationDetails.getUser_id());
            }
            if (registrationDetails.getConference_id() != null) {
                registration.setConference_id(registrationDetails.getConference_id());
            }
            if (registrationDetails.getRegistration_type() != null) {
                registration.setRegistration_type(registrationDetails.getRegistration_type());
            }
            if (registrationDetails.getEarly_bird() != null) {
                registration.setEarly_bird(registrationDetails.getEarly_bird());
            }
            if (registrationDetails.getPayment_status() != null) {
                registration.setPayment_status(registrationDetails.getPayment_status());
            }
            // Note: registered_at is typically not updated, as it's a timestamp for when the registration was made

            Registration updatedRegistration = registrationRepository.save(registration);
            return ResponseEntity.ok(updatedRegistration);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a registration
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        if (registrationRepository.existsById(id)) {
            registrationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}