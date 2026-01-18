package com.aarav.orderservice.repository;

import com.aarav.orderservice.entity.RefreshToken;
import com.aarav.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
