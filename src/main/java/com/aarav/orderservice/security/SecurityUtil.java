package com.aarav.orderservice.security;

import com.aarav.orderservice.entity.User;
import com.aarav.orderservice.exception.BadRequestException;
import com.aarav.orderservice.exception.UnauthorizedException;
import com.aarav.orderservice.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Unauthenticated access");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException("User not found")
        );
    }
}
