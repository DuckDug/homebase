package com.homebase.homebase.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceUpdateRequest {

    @NotNull(message = "emailNotificationsEnabled must be provided")
    private Boolean emailNotificationsEnabled;
}
