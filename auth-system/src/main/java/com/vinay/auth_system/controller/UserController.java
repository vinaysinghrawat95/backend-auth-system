package com.vinay.auth_system.controller;

import com.vinay.auth_system.dto.AuthResponse;
import com.vinay.auth_system.dto.LoginRequestDTO;
import com.vinay.auth_system.dto.SignupRequestDTO;
import com.vinay.auth_system.dto.UserResponseDTO;
import com.vinay.auth_system.entity.UserPrincipal;
import com.vinay.auth_system.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin("http://127.0.0.1:5500/")
@RestController()
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public String test(){
        System.out.println("TEST HIT");
        return "OK";
    }

    @GetMapping("/home")
    public String greet(){
        return "Hello I'm Vinay Singh Rawat";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@Valid  @RequestBody SignupRequestDTO signupRequestDTO){
        userService.signupUser(signupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        System.out.println("Login Success");
        String token = userService.loginUser(loginRequestDTO);
        return new ResponseEntity<>(new AuthResponse(token) , HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication){
        String email = authentication.getName();
        System.out.println("In controller email "+email);
        UserResponseDTO userResponseDTO = userService.getProfile(email);
        return ResponseEntity.ok(userResponseDTO);
    }

}
