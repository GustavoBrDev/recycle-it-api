package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.RequestDTO;
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
public class RegularUserPutRequestDTO implements RequestDTO {

    @NotBlank
    @StrongPassword
    public String password;

    @NotBlank
    public Avatar avatar;

    @NotBlank
    public String name;
}
