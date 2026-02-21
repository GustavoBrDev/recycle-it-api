package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.security.config.StrongPassword;
import com.ifsc.ctds.stinghen.recycle_it_api.security.models.UserCredentials;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RegularUserRequestDTO implements ConvertibleRequestDTO<RegularUser> {

    @NotBlank
    @Size(min = 5, max = 100, message = "Email inválido")
    public String email;

    @NotBlank
    @StrongPassword
    public String password;

    @NotBlank
    public Avatar avatar;

    @NotBlank
    public String name;

    @Override
    public RegularUser convert() {
        return RegularUser.builder()
                .name(name)
                .credential(
                        UserCredentials.builder()
                                .enabled(true)
                                .accountNonLocked(true)
                                .accountNonExpired(true)
                                .email(email)
                                .password(password)
                                .build()
                )
                .currentAvatar(avatar)
                .build();
    }
}
