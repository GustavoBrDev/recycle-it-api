package com.ifsc.ctds.stinghen.recycle_it_api.security.dtos;

import com.ifsc.ctds.stinghen.recycle_it_api.security.config.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de login
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO {

    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

    @NotBlank ( message = "Senha obrigatória")
    @StrongPassword
    private String password;
}
