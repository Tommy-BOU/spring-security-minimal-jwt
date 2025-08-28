package com.example.demo.controller;

import com.example.demo.models.UserApp;
import com.example.demo.repositories.UserAppRepository;
import com.example.demo.services.JwtService;
import com.example.demo.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserApp userApp) throws Exception {
        UserApp userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional != null && userAppOptional.getPassword().equals(userApp.getPassword())) {
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtService.createAuthenticationToken(userApp).toString()).body("connected");
        }
        throw new Exception();
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserApp userApp) throws Exception {
        UserApp userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional == null) {
            myUserDetailsService.createUser(userApp.getUsername(), userApp.getPassword());
        }else{
            throw new Exception();
        }

        return "User crée avec succès !";

    }
}
