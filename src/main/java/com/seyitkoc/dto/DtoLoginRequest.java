package com.seyitkoc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DtoLoginRequest {

    @NotBlank(message = "Email is required!")
    @Email(message = "Email must be email type!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, max = 24, message = "Password must be between 8 and 24 characters!")
    private String password;

}
