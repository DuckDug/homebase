package com.homebase.homebase.service;

import com.homebase.homebase.dto.UserContextResponse;
import com.homebase.homebase.exception.ResourceNotFoundException;
import com.homebase.homebase.model.User;
import com.homebase.homebase.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserContextService {
    private final UserRepository userRepository;

    public UserContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserContextResponse> userContext(@NonNull Authentication authentication) {

        Optional<User> user = userRepository.findByUsername(authentication.getName());
        return user.map(this::mapToUserContextResponse);
    }

    public Long getUserId(@NonNull Authentication authentication) {
        return userContext(authentication)
                .map(UserContextResponse::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User", authentication.getName()));
    }

    private UserContextResponse mapToUserContextResponse(@NonNull User user) {
        return new UserContextResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
