package com.seyitkoc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserIU {

    @NotBlank(message = "First name is required!")
    @Size(min = 3, max= 50, message = "First name must be between 3 and 50 characters!")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    @Size(min = 3, max= 50, message = "Last name must be between 3 and 50 characters!")
    private String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email must be email type!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, max = 24, message = "Password must be between 8 and 24 characters!")
    private String password;
}
