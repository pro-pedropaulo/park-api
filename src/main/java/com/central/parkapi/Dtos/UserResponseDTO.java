package com.central.parkapi.Dtos;

import com.central.parkapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id_user;
    private String username;
    private String role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String createdBy;
    private String updatedBy;
}