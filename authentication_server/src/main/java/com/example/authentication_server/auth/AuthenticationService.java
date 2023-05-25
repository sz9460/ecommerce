package com.example.authentication_server.auth;


import com.example.authentication_server.config.JwtService;
import com.example.authentication_server.kafka.Producer;
import com.example.authentication_server.model.EmailResetPassword;
import com.example.authentication_server.model.ResetPassword;
import com.example.authentication_server.user.*;
import com.example.basedomains.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  @Autowired
  private Producer producer;

  @Autowired
  private EmailService emailService;

  @Autowired
  private ConfirmationRepository confirmationTokenRepository;

  public AuthenticationResponse register(RegisterRequest request) {
     String jwtToken="";
    if(repository.findByEmail(request.getEmail()).isPresent()){
      jwtToken="existing user with this email";
    }
    else{
      var user = User.builder()
              .firstname(request.getFirstname())
              .lastname(request.getLastname())
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .role(Role.ROLE_USER).isEnabled(false)
              .build();
      repository.save(user);

      ConfirmationToken confirmationToken = new ConfirmationToken(user);
      confirmationTokenRepository.save(confirmationToken);
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setFrom("sofienezemmour999@gmail.com");
      mailMessage.setText("To confirm your account, please click here : "
              +"http://localhost:7777/authentication/user/confirm-account?token="+confirmationToken.getConfirmationToken());
      emailService.sendEmail(mailMessage);

      jwtToken = "please chek your email ti confirm registration";
    }
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();

  }
  public String putEmail(EmailResetPassword emailResetPassword) {
    User user = repository.findByEmailIgnoreCase(emailResetPassword.getEmail());
    if (user.isEnabled()){
      ConfirmationToken confirmationToken = new ConfirmationToken(user);
      confirmationTokenRepository.save(confirmationToken);
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setFrom("sofienezemmour999@gmail.com");
      mailMessage.setText("To reset your password, please click here : "
              +"http://localhost:7777/authentication/user/resetPassword?token="+confirmationToken.getConfirmationToken());
      emailService.sendEmail(mailMessage);
      return "we send a link to your email please ckeck";
    }

    else {
      return  "chek if your are regiter to our appliacation";
    }
  }
  public String resetPassword(ResetPassword resetPassword, String ConfirmToken){
    String message="";
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(ConfirmToken);
    if(token != null)
    {
      User user = repository.findByEmailIgnoreCase(token.getUserEntity().getEmail());
      user.setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
      repository.save(user);
      message="your password is changed";
    }
    else
    {
      message="The link is invalid or broken!";
    }
    return message;
  }


  public String confirmUserAccount(String ConfirmToken ){
    String message="";
    ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(ConfirmToken);
    if(token != null)
    {
      User user = repository.findByEmailIgnoreCase(token.getUserEntity().getEmail());
      user.setEnabled(true);
      repository.save(user);
      UserEvent userEvent =new UserEvent();
      userEvent.setIdUser(user.getId());
      userEvent.setMessage("new user created");
      producer.sendMessage(userEvent);
      message="your account is veirified";
    }
    else
    {
    message="The link is invalid or broken!";
    }
    return message;

  }


  public AuthenticationResponse registerAdmin (RegisterRequest request) {
    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ROLE_ADMIN)
            .isEnabled(true)
            .build();
    repository.save(user);
    var jwtToken = jwtService.generateToken(user.getEmail(),user.getRole().name(), user.getId());
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow();
    if(user.isEnabled()){
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
      var jwtToken = jwtService.generateToken(user.getEmail(),user.getRole().name(), user.getId());

      return AuthenticationResponse.builder()
              .token(jwtToken)
              .build();
    }
    else {
      return AuthenticationResponse.builder()
              .token("you need to confirm your registration")
              .build();
    }

  }
}
