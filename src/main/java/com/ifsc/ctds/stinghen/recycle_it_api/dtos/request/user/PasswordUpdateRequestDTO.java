package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.RequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.security.config.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequestDTO implements RequestDTO {

    @NotBlank
    public String senhaAtual;

    @NotBlank
    @StrongPassword
    public String novaSenha;
}
