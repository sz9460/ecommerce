package com.example.authentication_server;
import com.example.authentication_server.user.Role;
import com.example.authentication_server.user.User;
import com.example.authentication_server.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthenticationServerApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServerApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception{
        userRepository.save(
                new User(null,"sofien","zemmour","sofienezemmour999@gmail.com",passwordEncoder.encode("1234"),null,null,true, Role.ROLE_ADMIN));
        userRepository.save(
                new User(null,"baha","zemmour","s.zemmour@esi-sba.dz",passwordEncoder.encode("1234"),null,null,true, Role.ROLE_USER));

    }

}

