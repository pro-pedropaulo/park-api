package com.central.parkapi.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.central.parkapi.Utils.Util.ACTUAL_PASSWORD_IS_REQUIRED;
import static com.central.parkapi.Utils.Util.PASSWORD_MUST_BE_6_CHARACTERS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAlterPasswordDTO {


    @NotBlank(message = ACTUAL_PASSWORD_IS_REQUIRED)
    @Size(min = 6, max = 6, message = PASSWORD_MUST_BE_6_CHARACTERS)
    private String actualPassword;

    @NotBlank(message = ACTUAL_PASSWORD_IS_REQUIRED)
    @Size(min = 6, max = 6, message = PASSWORD_MUST_BE_6_CHARACTERS)
    private String newPassword;

    @NotBlank(message = ACTUAL_PASSWORD_IS_REQUIRED)
    @Size(min = 6, max = 6, message = PASSWORD_MUST_BE_6_CHARACTERS)
    private String confirmPassword;

}
