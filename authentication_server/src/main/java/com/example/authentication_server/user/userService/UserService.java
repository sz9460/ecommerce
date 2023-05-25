package com.example.authentication_server.user.userService;

import com.example.authentication_server.config.JwtService;
import com.example.authentication_server.user.User;
import com.example.authentication_server.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @NonNull HttpServletRequest request;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public User UpdateUser(User user,Integer id){
        final String authHeader = request.getHeader("Authorization");
        String jwt=null;
        String userEmail=null;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        var user1 = userRepository.findByEmail(userEmail).orElseThrow();
        if(user1.getId()== id) {
           if(user.getFirstname()==null){
               user1.setFirstname(user1.getFirstname());
           }else { user1.setFirstname(user.getFirstname()); }

           if(user.getLastname()==null){
               user1.setLastname(user1.getLastname());
           }else { user1.setLastname(user.getLastname()); }

           if(user.getEmail()==null){
               user1.setEmail(user1.getEmail());
           }else { user1.setEmail(user.getEmail()); }

           if(user.getPassword()==null){
               user1.setPassword(user1.getPassword());
           }else { user1.setPassword(passwordEncoder.encode(user.getPassword())); }
           user1.setRole(user1.getRole());
           return userRepository.save(user1);
       }
       else {
           return  null;
       }


    }




}
