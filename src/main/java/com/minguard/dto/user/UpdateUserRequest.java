package com.minguard.dto.user;

import com.minguard.dto.user.PasswordValidation;
import com.minguard.util.ValidationMessage;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest implements PasswordValidation {
    @Email(message = "email has incorrect format")
    private String email;
    
    @Pattern(message = "password must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit", regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}")
    private String password;
    
    @Pattern(message = "passwordConfirm must contain at least 8 characters, 1 uppercase letter, 1 lowercase letter and 1 digit", regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}")
    private String passwordConfirm;

    private String firstName;

    private String lastName;

    @Pattern(message = "mobilePrefix must contain plus sign and digits", regexp = "^[+]\\d+$")
    private String mobilePrefix;

    @Pattern(message = "mobileNumber must contain numbers", regexp = "\\d+$")
    private String mobileNumber;

    private Long genderId;

}
