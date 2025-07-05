package com.codegym.kfcbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeDefaultPasswordRequest {
    private String username;
    private String currentPassword;
    private String newPassword;
}
