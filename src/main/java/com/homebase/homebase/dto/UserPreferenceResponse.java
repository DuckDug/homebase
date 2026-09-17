package com.homebase.homebase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferenceResponse {

    private Long id;
    private Long userId;
    private boolean emailNotificationsEnabled;
}
