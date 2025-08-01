package com.souldevec.security.controllers;

import com.souldevec.security.dtos.LoginUserDto;
import com.souldevec.security.dtos.NewUserDto;
import com.souldevec.security.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> healthCheck() {
        logger.info("Health check endpoint accessed.");
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Welcome to the API!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserDto loginUserDto, BindingResult bindingResult){
        logger.info("Attempting to login user: {}", loginUserDto.getUserName());
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Revise sus credenciales");
        }
        try {
            String jwt = authService.authenticate(loginUserDto.getUserName(), loginUserDto.getPassword());
            logger.info("User '{}' logged in successfully.", loginUserDto.getUserName());
            return ResponseEntity.ok(jwt);
        } catch (Exception e){
            logger.error("Login failed for user: {}", loginUserDto.getUserName(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody NewUserDto newUserDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Revise los campos");
        }
        try {
            authService.registerUser(newUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registrado");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuth(){
            return ResponseEntity.ok().body("Autenticado");
    }
}
