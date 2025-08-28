package com.example.demo.controller;

import com.example.demo.models.UserApp;
import com.example.demo.repositories.UserAppRepository;
import com.example.demo.services.JwtService;
import com.example.demo.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserAppRepository userAppRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserApp userApp) throws Exception {
        UserApp userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional != null && passwordEncoder.matches(userApp.getPassword(), userAppOptional.getPassword())) {
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtService.createAuthenticationToken(userAppOptional).toString()).body("connected");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nom d'utilisateur ou mot de passe invalide");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute UserApp userApp) throws Exception {
        UserApp userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional == null) {
            myUserDetailsService.createUser(userApp.getUsername(), userApp.getPassword());
            return ResponseEntity.ok().body("User crée avec succès !");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'utilisateur existe déjà");
        }


    }
}
