package com.homebase.homebase.controller;

import com.homebase.homebase.dto.UserPreferenceResponse;
import com.homebase.homebase.dto.UserPreferenceUpdateRequest;
import com.homebase.homebase.model.UserPreference;
import com.homebase.homebase.service.UserContextService;
import com.homebase.homebase.service.UserPreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;
    private final UserContextService userContextService;

    public UserPreferenceController(UserPreferenceService userPreferenceService, UserContextService userContextService) {
        this.userPreferenceService = userPreferenceService;
        this.userContextService = userContextService;
    }

    @GetMapping
    public ResponseEntity<UserPreferenceResponse> getPreferences(Authentication authentication) {
        Long userId = userContextService.getUserId(authentication);
        UserPreference userPreference = userPreferenceService.getPreferences(userId);
        return ResponseEntity.ok().body(mapToUserPreferenceResponse(userPreference));
    }

    @PatchMapping
    public ResponseEntity<UserPreferenceResponse> updatePreferences(
            @Valid @RequestBody UserPreferenceUpdateRequest request,
            Authentication authentication
    ) {
        Long userId = userContextService.getUserId(authentication);
        UserPreference updated = userPreferenceService.updatePreferences(userId, request.getEmailNotificationsEnabled());
        return ResponseEntity.ok().body(mapToUserPreferenceResponse(updated));
    }

    private UserPreferenceResponse mapToUserPreferenceResponse(UserPreference userPreference) {
        return new UserPreferenceResponse(
            userPreference.getId(),
            userPreference.getUserId(),
            userPreference.isEmailNotificationsEnabled()
        );
    }
}
