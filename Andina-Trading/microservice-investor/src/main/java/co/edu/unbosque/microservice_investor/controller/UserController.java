package co.edu.unbosque.microservice_investor.controller;

import co.edu.unbosque.microservice_investor.model.dto.LoginResponse;
import co.edu.unbosque.microservice_investor.model.dto.PortfolioDTO;
import co.edu.unbosque.microservice_investor.model.dto.UserDTO;
import co.edu.unbosque.microservice_investor.model.dto.UserLoginDTO;
import co.edu.unbosque.microservice_investor.model.enums.AccountStatus;
import co.edu.unbosque.microservice_investor.model.enums.Role;
import co.edu.unbosque.microservice_investor.security.JwtUtil;
import co.edu.unbosque.microservice_investor.service.PortfolioService;
import co.edu.unbosque.microservice_investor.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investor")

public class UserController {

    private final UserService userService;
    private final PortfolioService portfolioService;

    @Autowired
    private JwtUtil jwtUtil;

    public UserController( UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/portfolio/{accountId}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable String accountId) {
        PortfolioDTO portfolio = portfolioService.getPortfolio(accountId);
        if (portfolio != null) {
            return ResponseEntity.ok(portfolio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }



    // Get all users
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Register a new user
    @PostMapping
    public UserDTO registerUser(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserLoginDTO request) {

        try {
            LoginResponse response = userService.loginUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            LoginResponse errorResponse = new LoginResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);
            UserDTO user = userService.getUserById(userId);
            user.setPassword(null);
            user.setPasswordHash(null);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }




    // Find by email
    @GetMapping("/find")
    public UserDTO getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    // Check if email exists
    @GetMapping("/exists")
    public boolean checkIfEmailExists(@RequestParam String email) {
        return userService.emailExists(email);
    }

    // Get users by role
    @GetMapping("/role")
    public List<UserDTO> getUsersByRole(@RequestParam Role role) {
        return userService.getUsersByRole(role);
    }

    // Get users by account status
    @GetMapping("/status")
    public List<UserDTO> getUsersByStatus(@RequestParam AccountStatus status) {
        return userService.getUsersByStatus(status);
    }

    // Get users with active subscriptions
    @GetMapping("/subscribed")
    public List<UserDTO> getSubscribedUsers() {
        return userService.getUsersWithSubscription();
    }

    // Search users by name (partial match)
    @GetMapping("/search")
    public List<UserDTO> searchUsersByName(@RequestParam String name) {
        return userService.searchUsersByName(name);
    }

    @PutMapping("/me/preferences")
    public ResponseEntity<Void> updateUserPreferences(@RequestHeader("Authorization") String authHeader,
                                                      @RequestBody UserDTO userDTO) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(token);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            userDTO.setId(userId);
            userService.updateUserPreferences(userDTO);
            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}