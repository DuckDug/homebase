package com.homebase.homebase.service;

import com.homebase.homebase.model.UserPreference;
import com.homebase.homebase.repository.UserPreferenceRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    public boolean isEmailNotificationsEnabled(Long userId) {
        return userPreferenceRepository.findByUserId(userId)
                .map(UserPreference::isEmailNotificationsEnabled)
                .orElse(true);
    }

    public UserPreference getPreferences(Long userId) {
        return userPreferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));
    }

    public UserPreference updatePreferences(Long userId, Boolean emailNotificationsEnabled) {
        UserPreference userPreference = userPreferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));
        userPreference.setEmailNotificationsEnabled(emailNotificationsEnabled);
        return userPreferenceRepository.save(userPreference);
    }

    private UserPreference createDefaultPreferences(Long userId) {
        UserPreference newUserPreference = UserPreference.builder()
                .userId(userId)
                .emailNotificationsEnabled(true)
                .build();
        return userPreferenceRepository.save(newUserPreference);
    }
}
