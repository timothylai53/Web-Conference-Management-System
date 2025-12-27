package com.webcrafters.confease_backend.controller;

import com.webcrafters.confease_backend.model.User;
import com.webcrafters.confease_backend.model.UserRole;
import com.webcrafters.confease_backend.model.Role;
import com.webcrafters.confease_backend.repository.UserRepository;
import com.webcrafters.confease_backend.repository.UserRoleRepository;
import com.webcrafters.confease_backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        User user = userOpt.get();

        // Check password (note: in production, you should use password hashing)
        if (!user.getPassword_hash().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        // Get user's role from user_role table
        String roleName = "Author"; // Default role
        try {
            // Find user_role entries for this user
            List<UserRole> userRoles = userRoleRepository.findByUserId(user.getUser_id());
            
            if (!userRoles.isEmpty()) {
                // Get the first role (users typically have one primary role)
                Integer roleId = userRoles.get(0).getRole_id();
                Optional<Role> roleOpt = roleRepository.findById(roleId);
                if (roleOpt.isPresent()) {
                    roleName = roleOpt.get().getRole_name();
                }
            }
        } catch (Exception e) {
            // If role lookup fails, use default "Author"
            System.err.println("Error fetching role for user " + user.getUser_id() + ": " + e.getMessage());
        }

        // Return user info (without password)
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_id", user.getUser_id());
        userMap.put("email", user.getEmail());
        userMap.put("first_name", user.getFirst_name());
        userMap.put("last_name", user.getLast_name());
        userMap.put("affiliation", user.getAffiliation());
        userMap.put("country", user.getCountry());
        userMap.put("role", roleName);
        userMap.put("category", user.getCategory());
        userMap.put("orcid", user.getOrcid());
        
        response.put("user", userMap);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email already registered"));
        }

        // Set default values
        if (user.getIs_email_verified() == null) {
            user.setIs_email_verified(false);
        }

        // Save user
        User savedUser = userRepository.save(user);
        
        // Assign default "Author" role to new user
        String roleName = "Author";
        try {
            Optional<Role> authorRoleOpt = roleRepository.findAll().stream()
                .filter(r -> r.getRole_name().equals("Author"))
                .findFirst();
            
            if (authorRoleOpt.isPresent()) {
                UserRole userRole = new UserRole();
                userRole.setUser_id(savedUser.getUser_id());
                userRole.setRole_id(authorRoleOpt.get().getRole_id());
                userRole.setAssigned_at(new java.sql.Timestamp(System.currentTimeMillis()));
                userRoleRepository.save(userRole);
            }
        } catch (Exception e) {
            // If role assignment fails, continue with default
        }

        // Return user info (without password)
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_id", savedUser.getUser_id());
        userMap.put("email", savedUser.getEmail());
        userMap.put("first_name", savedUser.getFirst_name());
        userMap.put("last_name", savedUser.getLast_name());
        userMap.put("role", roleName);
        
        response.put("user", userMap);
        return ResponseEntity.ok(response);
    }
}
