package com.vinay.auth_system.service;

import com.vinay.auth_system.dto.AuthResponse;
import com.vinay.auth_system.dto.LoginRequestDTO;
import com.vinay.auth_system.dto.SignupRequestDTO;
import com.vinay.auth_system.dto.UserResponseDTO;
import com.vinay.auth_system.entity.User;
import com.vinay.auth_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final JWTService jwtService;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final EmailService emailService;

    public void signupUser(SignupRequestDTO signupRequestDTO) {

        if(signupRequestDTO == null){
            throw new RuntimeException("Invalid signup request.");
        }

        if(!signupRequestDTO.getEmail().equals(signupRequestDTO.getEmail().toLowerCase())) {
            throw new RuntimeException("Invalid email format.");
        }

        if(userRepo.existsByEmail(signupRequestDTO.getEmail())){
            throw new RuntimeException("Email already used.");
        }

        String verificationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setUsername(signupRequestDTO.getUsername());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(encoder.encode(signupRequestDTO.getPassword()));
        user.setVerificationToken(verificationToken);
        user.setVerified(false);

        userRepo.save(user);

        emailService.sendVerificationEmail(signupRequestDTO.getEmail(), verificationToken);

    }

    public void verifyEmail(String token){
        User user = userRepo.findByVerificationToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid verification token"));

        if(user.isVerified()){
            throw new RuntimeException("User already verified");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepo.save(user);
    }


    public AuthResponse loginUser(LoginRequestDTO loginRequestDTO) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()
                    )
            );

            User user = userRepo.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));

            if(!user.isVerified()){
                throw new RuntimeException("Please verify your token first");
            }

            String refreshToken = jwtService.generateRefreshToken();
            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));
            userRepo.save(user);

            String accessToken = jwtService.generateToken(loginRequestDTO.getEmail());
            return new AuthResponse(accessToken, refreshToken);

        }catch (BadCredentialsException badCredentialsException){
            throw new BadCredentialsException("Invalid Credential");
        }
    }

    public UserResponseDTO getProfile(String email){
        log.info("Inside getProfile service");
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new UserResponseDTO(user.getId(),user.getUsername(), user.getEmail());
    }

    public AuthResponse refreshToken(String refreshToken){
        User user = userRepo.findByRefreshToken(refreshToken)
                .orElseThrow(()->new RuntimeException("Invalid refresh token"));

        if(user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Refresh token expired, Please login again");
        }

        String newAccessToken = jwtService.generateToken(user.getEmail());
        return new AuthResponse(newAccessToken, refreshToken);
    }

    public void logout(String refreshToken){
        User user = userRepo.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("Invalid refresh token"));

        user.setRefreshToken(null);
        user.setRefreshTokenExpiry(null);
        userRepo.save(user);
    }
}
