package com.central.parkapi.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.central.parkapi.entity.User.Role;

import java.time.LocalDateTime;

import static com.central.parkapi.Utils.Util.ACTUAL_PASSWORD_IS_REQUIRED;
import static com.central.parkapi.Utils.Util.PASSWORD_MUST_BE_6_CHARACTERS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private Long id_user;
    @NotBlank(message = "Username is required")
    @Email(message = "Invalid email")
    private String username;

    @NotBlank(message = ACTUAL_PASSWORD_IS_REQUIRED)
    @Size(min = 6, max = 6, message = PASSWORD_MUST_BE_6_CHARACTERS)
    private String password;

    private Role role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String createdBy;
    private String updatedBy;
}
