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

import java.util.Map;

@AllArgsConstructor
@RestController()
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @GetMapping("/greet")
    public String greet(){
        return "Hello I'm Vinay Singh Rawat";
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signupUser(@Valid  @RequestBody SignupRequestDTO signupRequestDTO){
        userService.signupUser(signupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message","Registration successfully, Please check you email to verify your account"));
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token){
        userService.verifyEmail(token);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully!", "type", "success"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        AuthResponse authResponse = userService.loginUser(loginRequestDTO);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @GetMapping("/home")
    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication){
        String email = authentication.getName();
        System.out.println("In controller email "+email);
        UserResponseDTO userResponseDTO = userService.getProfile(email);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> request){
        String refreshToken = request.get("refreshToken");
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request){
        String refreshToken = request.get("refreshToken");
        userService.logout(refreshToken);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully", "type", "success"));
    }

}
