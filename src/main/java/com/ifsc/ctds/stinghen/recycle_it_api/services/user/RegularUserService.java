package com.ifsc.ctds.stinghen.recycle_it_api.services.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.user.RegularUserRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.security.repository.UserCredentialsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service para os métodos específicos do usuário regular
 */
@AllArgsConstructor
@Service
public class RegularUserService {

    public RegularUserRepository repository;
    public UserCredentialsRepository credentialsRepository;

    @Transactional
    public ResponseDTO create (RegularUserRequestDTO requestDTO ){
        RegularUser user = requestDTO.convert();

        if ( credentialsRepository.existsByEmail( requestDTO.email ) ){
            return FeedbackResponseDTO.builder()
                    .mainMessage("Não foi possível realizar o cadastro")
                    .content("Já existe uma conta com esse e-mail")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Cadastro Efetuado")
                .isAlert(false)
                .isError(false)
                .build();
    }

    @Transactional
    public RegularUser getObjectById ( Long id ){
        return repository.findById(id).get();
    }
}
