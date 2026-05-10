package com.vinay.auth_system.repository;

import com.vinay.auth_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.OptionalInt;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User>findByRefreshToken(String refreshToken);

    Optional<User> findByVerificationToken(String token);
}
