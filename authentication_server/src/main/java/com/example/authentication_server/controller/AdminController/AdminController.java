package com.example.authentication_server.controller.AdminController;

import com.example.authentication_server.auth.AuthenticationResponse;
import com.example.authentication_server.auth.AuthenticationService;
import com.example.authentication_server.auth.RegisterRequest;
import com.example.authentication_server.user.User;
import com.example.authentication_server.user.UserRepository;
import jakarta.ws.rs.GET;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {
    @Autowired
    private final AuthenticationService service;

    @Autowired
    private UserRepository userRepository;
    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.registerAdmin(request));
    }
    @PostMapping("/registerFlockeur")
    public ResponseEntity<AuthenticationResponse> registerFlockeur(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.registerFlockeur(request));
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }
    @DeleteMapping("/delteUser/{id}")
    public ResponseEntity <HttpStatus> deleteUser(@PathVariable Integer id){
        userRepository.deleteById(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}
