package com.vinay.auth_system.service;

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

@Service
@AllArgsConstructor
public class UserService {

    private final JWTService jwtService;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    public void signupUser(SignupRequestDTO signupRequestDTO) {

        if(!signupRequestDTO.getEmail().equals(signupRequestDTO.getEmail().toLowerCase())){
            throw new RuntimeException("Invalid email format.");
        }
        if(signupRequestDTO == null){
            throw new RuntimeException("Invalid signup request.");
        }

        if(userRepo.existsByEmail(signupRequestDTO.getEmail())){
            throw new RuntimeException("Email already used.");
        }
        User user = new User();
        user.setUsername(signupRequestDTO.getUsername());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(encoder.encode(signupRequestDTO.getPassword()));

        userRepo.save(user);

    }

    public String loginUser(LoginRequestDTO loginRequestDTO) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()
                    )
            );
            return jwtService.generateToken(loginRequestDTO.getEmail());

        }catch (BadCredentialsException badCredentialsException){
            throw new BadCredentialsException("Invalid Credential");
        }
    }

    public UserResponseDTO getProfile(String email) {
        System.out.println("Inside getProfile service : "+email);
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        System.out.println("Inside getProfile after fetching db "+ user);
        return new UserResponseDTO(user.getId(),user.getUsername(), user.getEmail());
    }
}
