package com.homebase.homebase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserContextResponse {
    private Long id;
    private String username;
    private String email;
}
