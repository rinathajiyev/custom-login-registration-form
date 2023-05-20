package com.darkrinta.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {

    private String phone;
    private String password;
    private String confirmPassword;
}
