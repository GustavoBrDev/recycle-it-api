package com.ifsc.ctds.stinghen.recycle_it_api.services.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserPutRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.FullUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.BadValueException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.InvalidRelationshipException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.Article;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.user.RegularUserRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.security.repository.UserCredentialsRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.article.ArticleService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.league.LeagueService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.project.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos do usuário padrão (regular)
 * @author Gustavo Stinghen
 * @see RegularUser
 * @see User
 * @see PasswordEncoder
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class RegularUserService {

    public RegularUserRepository repository;
    public UserCredentialsRepository credentialsRepository;
    public PasswordEncoder passwordEncoder;
    public LeagueService leagueService;
    public ProjectService projectService;
    public ArticleService articleService;

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
        user.getCredential().setPassword( passwordEncoder.encode(user.getCredential().getPassword()));

        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Usuário atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o avatar de um usuário
     * @param id o id do usuário
     * @param avatar o novo avatar
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO editAvatar (Long id, Avatar avatar ){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.setCurrentAvatar(avatar);
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);
    }

    /**
     * Atualiza o nome de um usuário
     * @param id o id do usuário
     * @param name o novo nome
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO editName (Long id, String name){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.setName(name);
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)

                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);
    }

    /**
     * Atualiza a senha de um usuário
     * @param id o id do usuário
     * @param password a nova senha
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO editPassword (Long id, String password){


        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.getCredential().setPassword(passwordEncoder.encode(password));
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);

    }

    /**
     * Atualiza o email de um usuário
     * @param id o id do usuário
     * @param email o novo email
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO editEmail (Long id, String email){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.getCredential().setEmail(email);
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);

    }

    /**
     * Atualiza a quantidade de recycle gems de um usuário
     * @param id o id do usuário
     * @param gems a nova quantidade de recycle gems
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO editGems ( Long id, Long gems ){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.setRecycleGems(gems);
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);
    }

    /**
     * Incrementa a quantidade de recyle gems de um usuário
     * @param id o id do usuário
     * @param amount a quantidade de recyle gems a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO incrementGems ( Long id, Long amount ){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.setRecycleGems(user.getRecycleGems() + amount);
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);
    }


    /**
     * Decrementa a quantidade de recyle gems de um usuário
     * @param id o id do usuário
     * @param amount a quantidade de recyle gems a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     * @throws BadValueException quando o usuário não tiver saldo suficiente
     */
    @Transactional
    public ResponseDTO decrementGems ( Long id, Long amount ){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();


            if ( user.getRecycleGems() < amount ){
                user.setRecycleGems(0L);
                repository.save(user);
                throw new BadValueException("Usuário não tem saldo suficiente");
            }else{
                user.setRecycleGems(user.getRecycleGems() - amount);
            }

            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);
    }

    /**
     * Atualiza a liga atual de um usuário pelo ID
     * @param id o id do usuário
     * @param leagueId o id da nova liga
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO editLeagueById ( Long id, Long leagueId ){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.setActualLeague(leagueService.getObjectById(leagueId));
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);

    }

    /**
     * Atualiza a liga atual de um usuário pelo tier
     * @param id o id do usuário
     * @param tier o tier da nova liga
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO editLeagueByTier ( Long id, int tier ){

        if ( repository.existsById(id) ){

            RegularUser user = repository.findById(id).get();
            user.setActualLeague(leagueService.getObjectByTier(tier));
            repository.save(user);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com id: " + id);

    }

    /**
     * Adiciona um usuário como amigo
     * @param userId o ID do usuário base
     * @param friendId o ID do usuário a ser adicionado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException caso um dos usuários não seja encontrado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO addFriend ( Long userId, Long friendId ){

        RegularUser user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + userId));

        RegularUser friend = repository.findById(friendId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + friendId));

        user.addFriend(friend);
        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Novo Amigo")
                .content("A nova amizade foi registrada corretamente")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Remove um usuário como amigo
     * @param userId o ID do usuário base
     * @param friendId o ID do usuário a ser removido
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException caso um dos usuários não seja encontrado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO removeFriend ( Long userId, Long friendId ){

        RegularUser user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + userId));

        RegularUser friend = repository.findById(friendId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + friendId));

        user.removeFriend(friend);
        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Usuário Removido")
                .content("O usuário foi removido da lista de amizades")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Marca um projeto como em andamento
     * @param userId o ID do usuário base
     * @param projectId o ID do projeto a ser adicionado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException caso o usuário ou o projeto não seja encontrado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO addProject ( Long userId, Long projectId ){

        RegularUser user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + userId));

        Project project = projectService.getObjectById(projectId);

        user.addProject(project);
        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Projeto Iniciado")
                .content("O projeto foi adicionado á lista de projetos em andamento")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Desmarca um projeto como em andamento
     * @param userId o ID do usuário base
     * @param projectId o ID do projeto a ser removido
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException caso o usuário ou o projeto não seja encontrado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO removeProject ( Long userId, Long projectId ){

        RegularUser user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + userId));

        Project project = projectService.getObjectById(projectId);

        user.removeProject(project);
        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Projeto Removido")
                .content("O projeto foi removido da lista de projetos em andamento")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Marca um artigo como lido
     * @param userId o ID do usuário base
     * @param articleId o ID do artigo a ser adicionado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException caso o usuário ou o artigo não seja encontrado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO addArticle ( Long userId, Long articleId ){

        RegularUser user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + userId));

        Article article = articleService.getObjectById(articleId);

        user.addArticle(article);
        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Artigo Lido")
                .content("O artigo foi adicionado á lista de artigos lidos")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Desmarca um artigo como lido
     * @param userId o ID do usuário base
     * @param articleId o ID do artigo a ser removido
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException caso o usuário ou o artigo não seja encontrado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO removeArticle ( Long userId, Long articleId ){

        RegularUser user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + userId));

        Article article = articleService.getObjectById(articleId);

        user.removeArticle(article);
        repository.save(user);

        return FeedbackResponseDTO.builder()
                .mainMessage("Artigo Removido")
                .content("O artigo foi removido da lista de artigos lidos")
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

    /**
     * Obtém todos os usuários
     * @return lista de usuários em forma de {@link RegularUser}
     */
    @Transactional (readOnly = true)
    public List<RegularUser> getAll (){
        return repository.findAll();
    }

    /**
     * Obtém todos os usuários de forma simplificada
     * @return lista de usuários em forma de {@link SimpleUserResponseDTO}
     */
    @Transactional (readOnly = true)
    public List<SimpleUserResponseDTO> getAllSimple (){
        return repository.findAll().stream().map(SimpleUserResponseDTO::new).toList();
    }

    /**
     * Obtém todos os usuários de forma completa
     * @return lista de usuários em forma de {@link FullUserResponseDTO}
     */
    @Transactional (readOnly = true)
    public List<FullUserResponseDTO> getAllFull (){
        return repository.findAll().stream().map(FullUserResponseDTO::new).toList();
    }

    /**
     * Obtém todos os usuários de forma paginada
     * @param pageable as configurações de paginação
     * @return página de usuários em forma de {@link RegularUser} utilizando paginação {@link Page}
     */
    @Transactional (readOnly = true)
    public Page<RegularUser> getAll (Pageable pageable){
        return repository.findAll(pageable);
    }

    /**
     * Obtém todos os usuários de forma simplificada e paginada
     * @param pageable as configurações de paginação
     * @return página de usuários em forma de {@link SimpleUserResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional (readOnly = true)
    public Page<SimpleUserResponseDTO> getAllSimple ( Pageable pageable ){
        return repository.findAll(pageable).map(SimpleUserResponseDTO::new);
    }

    /**
     * Obtém todos os usuários de forma completa e paginada
     * @param pageable as configurações de paginação
     * @return página de usuários em forma de {@link FullUserResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional (readOnly = true)
    public Page<FullUserResponseDTO> getAllFull ( Pageable pageable ){
        return repository.findAll(pageable).map(FullUserResponseDTO::new);
    }

    /**
     * Deleta o usuário com base em seu ID
     * @param id o id do usuário a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById ( Long id ){

        if ( repository.existsById(id) ){
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário deletado")
                    .content("O usuário com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com o ID " + id );
    }

    /**
     * Verifica se existe um usuário pelo ID
     * @param id o ID a ser verificado
     * @return true se existir, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * Verifica se existe um usuário pelo e-mail
     * @param email o e-mail a ser verificado
     * @return true se existir, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return credentialsRepository.existsByEmail(email);
    }

    /**
     * Deleta o usuário com base em seu email
     * @param email o e-mail do usuário a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o usuário não for encontrado
     */
    @Transactional
    public ResponseDTO deleteByEmail ( String email ){

        if ( credentialsRepository.existsByEmail(email) ){

            User user = credentialsRepository.findByEmail(email).get().getUser();
            repository.deleteById(user.getId());

            return FeedbackResponseDTO.builder()
                    .mainMessage("Usuário deletado")
                    .content("O usuário com o e-mail " + email + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Usuário não encontrado com o e-mail " + email );
    }
}
