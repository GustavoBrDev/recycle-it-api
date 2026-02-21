package com.ifsc.ctds.stinghen.recycle_it_api.services.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserPutRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.FullUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.user.RegularUserRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.security.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para os métodos específicos do usuário padrão (regular)
 */
@AllArgsConstructor
@Service
public class RegularUserService {

    public RegularUserRepository repository;
    public UserCredentialsRepository credentialsRepository;
    public PasswordEncoder passwordEncoder;

    /**
     * Cria/persiste o registro de um usuário no banco de dados
     * @param requestDTO DTO de request de usuários
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
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

        user.getCredential().setPassword( passwordEncoder.encode(user.getCredential().getPassword()));
        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Cadastro Efetuado")
                .isAlert(false)
                .isError(false)
                .build();
    }

    @Transactional
    public ResponseDTO update(Long id, RegularUserPutRequestDTO requestDTO) {

        RegularUser user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado com id: " + id
                ));

        user.setName(requestDTO.name);
        user.setCurrentAvatar(requestDTO.avatar);
        user.getCredential().setPassword(requestDTO.password);

        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Usuário atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Obtem o objeto de Usuário pelo id fornecido
     * @param id o id a ser buscado
     * @return o usuário em forma de {@link RegularUser}
     */
    @Transactional (readOnly = true)
    public RegularUser getObjectById ( Long id ){
        return repository.findById(id).get();
    }

    /**
     * Obtém a response completa de um usuário pelo id fornecido
     * @param id o id a ser buscado
     * @return o usuário em forma da DTO {@link FullUserResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional (readOnly = true)
    public ResponseDTO getFullById ( Long id ){

        if ( repository.existsById(id) ){
            return new FullUserResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + id );
    }

    /**
     * Obtém a response breve de um usuário pelo id fornecido
     * @param id o id a ser buscado
     * @return o usuário em forma da DTO {@link SimpleUserResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional (readOnly = true)
    public ResponseDTO getSimpleById ( Long id ){

        if ( repository.existsById(id) ){
            return new SimpleUserResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + id );
    }

    /**
     * Obtem o objeto de Usuário pelo e-mail fornecido
     * @param email o e-mail a ser buscado
     * @return o usuário em forma de {@link RegularUser}
     */
    @Transactional (readOnly = true)
    public RegularUser getObjectByEmail ( String email ){
        return (RegularUser) credentialsRepository.findByEmail(email).get().getUser();
    }

    /**
     * Obtém a response completa de um usuário pelo e-mail fornecido
     * @param email o e-mail a ser buscado
     * @return o usuário em forma da DTO {@link FullUserResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional (readOnly = true)
    public ResponseDTO getFullByEmail ( String email ){

        if ( credentialsRepository.existsByEmail(email) ){
            User user = credentialsRepository.findByEmail(email).get().getUser();

            if ( user instanceof RegularUser ){
                return new FullUserResponseDTO( (RegularUser) user);
            }

        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email );
    }

    /**
     * Obtém a response breve de um usuário pelo e-mail fornecido
     * @param email o e-mail a ser buscado
     * @return o usuário em forma da DTO {@link SimpleUserResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional (readOnly = true)
    public ResponseDTO getSimpleByEmail ( String email ){

        if ( credentialsRepository.existsByEmail(email) ){
            User user = credentialsRepository.findByEmail(email).get().getUser();

            if ( user instanceof RegularUser ){
                return new SimpleUserResponseDTO( (RegularUser) user);
            }

        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email );
    }



}
