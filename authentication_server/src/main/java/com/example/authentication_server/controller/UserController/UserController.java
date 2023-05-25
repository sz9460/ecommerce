package com.example.authentication_server.controller.UserController;
import com.example.authentication_server.auth.AuthenticationResponse;
import com.example.authentication_server.auth.AuthenticationService;
import com.example.authentication_server.auth.RegisterRequest;
import com.example.authentication_server.model.EmailResetPassword;
import com.example.authentication_server.model.ResetPassword;
import com.example.authentication_server.user.User;
import com.example.authentication_server.user.userService.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
  @Autowired
  private final AuthenticationService service;
  @Autowired
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.register(request));
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<User> UpdateUser(@PathVariable Integer id,@RequestBody User user){
    return ResponseEntity.ok(userService.UpdateUser(user,id));
  }

  @GetMapping("/confirm-account")
  public ResponseEntity<String> confirmUserAccount(@RequestParam("token")String confirmationToken){
    return ResponseEntity.ok(service.confirmUserAccount(confirmationToken));
  }
  @PostMapping("/reset-Password")
  public ResponseEntity<String> putEmail(@RequestBody EmailResetPassword emailResetPassword) {
    return ResponseEntity.ok(service.putEmail(emailResetPassword));
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword,@RequestParam("token")String confirmationToken) {
    return ResponseEntity.ok(service.resetPassword(resetPassword,confirmationToken));
  }




}
