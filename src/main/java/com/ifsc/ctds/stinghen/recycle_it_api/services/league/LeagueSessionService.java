package com.ifsc.ctds.stinghen.recycle_it_api.services.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.LeagueSessionResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.UserPunctuationResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.LeagueSession;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.UserPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.league.LeagueSessionRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service para os métodos específicos de sessões de liga
 * @author Gustavo Stinghen
 * @see LeagueSession
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class LeagueSessionService {

    public LeagueSessionRepository repository;
    public RegularUserService userService;

    /**
     * Cria/persiste o registro de uma sessão de liga no banco de dados
     * @param session a sessão de liga a ser criada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(LeagueSession session) {
        repository.save(session);

        return FeedbackResponseDTO.builder()
                .mainMessage("Sessão de liga criada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza a data de início de uma sessão de liga
     * @param id o id da sessão de liga
     * @param startDate a nova data de início
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a sessão de liga não for encontrada
     */
    @Transactional
    public ResponseDTO editStartDate(Long id, LocalDate startDate) {
        if (repository.existsById(id)) {
            LeagueSession session = repository.findById(id).get();
            session.setStartDate(startDate);
            repository.save(session);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Sessão de liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Sessão de liga não encontrada com id: " + id);
    }

    /**
     * Atualiza a data de término de uma sessão de liga
     * @param id o id da sessão de liga
     * @param endDate a nova data de término
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a sessão de liga não for encontrada
     */
    @Transactional
    public ResponseDTO editEndDate(Long id, LocalDate endDate) {
        if (repository.existsById(id)) {
            LeagueSession session = repository.findById(id).get();
            session.setEndDate(endDate);
            repository.save(session);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Sessão de liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Sessão de liga não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de LeagueSession pelo id fornecido
     * @param id o id a ser buscado
     * @return a sessão de liga em forma de {@link LeagueSession}
     */
    @Transactional(readOnly = true)
    public LeagueSession getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de uma sessão de liga pelo id fornecido
     * @param id o id a ser buscado
     * @return a sessão de liga em forma da DTO {@link LeagueSessionResponseDTO}
     * @throws NotFoundException quando a sessão de liga não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new LeagueSessionResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Sessão de liga não encontrada com o ID " + id);
    }

    /**
     * Obtém todas as sessões de liga
     * @return lista de sessões de liga em forma de {@link LeagueSession}
     */
    @Transactional(readOnly = true)
    public List<LeagueSession> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as sessões de liga ordenadas por data de término
     * @param pageable as configurações de paginação
     * @return página de sessões de liga em forma de {@link LeagueSession} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<LeagueSession> getAllOrderedByEndDate(Pageable pageable) {
        return repository.findAllByOrderByEndDate(pageable);
    }

    /**
     * Obtém todas as sessões de liga de forma paginada
     * @param pageable as configurações de paginação
     * @return página de sessões de liga em forma de {@link LeagueSession} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<LeagueSession> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém sessões de liga por usuário de forma paginada
     * @param userId o id do usuário
     * @param pageable as configurações de paginação
     * @return página de sessões de liga em forma de {@link LeagueSession} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<LeagueSession> getByUserId(Long userId, Pageable pageable) {
        return repository.findByUsers_IdOrderByEndDate(userId, pageable);
    }

    /**
     * Obtém sessões de liga por email do usuário de forma paginada
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de sessões de liga em forma de {@link LeagueSession} utilizando paginação {@link Page}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public Page<LeagueSession> getByUserEmail(String email, Pageable pageable) {
        if (userService.existsByEmail(email)) {
            Long userId = userService.getObjectByEmail(email).getId();
            return repository.findByUsers_IdOrderByEndDate(userId, pageable);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Deleta a sessão de liga com base em seu ID
     * @param id o id da sessão de liga a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a sessão de liga não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Sessão de liga deletada")
                    .content("A sessão de liga com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Sessão de liga não encontrada com o ID " + id);
    }

    /**
     * Obtém a sessão de liga ativa de um usuário por ID
     * @param userId o ID do usuário
     * @return a sessão ativa em forma de {@link LeagueSession}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public LeagueSession getActiveSessionByUserId(Long userId) {
        return repository.findActiveSessionByUserId(userId, LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Não há sessão de liga ativa para o usuário com ID " + userId));
    }

    /**
     * Obtém a sessão de liga ativa de um usuário por email
     * @param email o email do usuário
     * @return a sessão ativa em forma de {@link LeagueSession}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public LeagueSession getActiveSessionByUserEmail(String email) {
        if (userService.existsByEmail(email)) {
            return repository.findActiveSessionByUserEmail(email, LocalDate.now())
                    .orElseThrow(() -> new NotFoundException("Não há sessão de liga ativa para o usuário com e-mail " + email));
        }
        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Obtém a response da sessão de liga ativa de um usuário por ID
     * @param userId o ID do usuário
     * @return a sessão ativa em forma da DTO {@link LeagueSessionResponseDTO}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public ResponseDTO getActiveSessionResponseByUserId(Long userId) {
        LeagueSession session = getActiveSessionByUserId(userId);
        return new LeagueSessionResponseDTO(session);
    }

    /**
     * Obtém a response da sessão de liga ativa de um usuário por email
     * @param email o email do usuário
     * @return a sessão ativa em forma da DTO {@link LeagueSessionResponseDTO}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public ResponseDTO getActiveSessionResponseByUserEmail(String email) {
        if (userService.existsByEmail(email)) {
            Long userId = userService.getObjectByEmail(email).getId();
            LeagueSession session = getActiveSessionByUserId(userId);
            return new LeagueSessionResponseDTO(session);
        }
        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Obtém o UserPunctuation da sessão de liga ativa de um usuário por ID
     * @param userId o ID do usuário
     * @return o UserPunctuation em forma de {@link UserPunctuation}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public UserPunctuation getActivePunctuationByUserId(Long userId) {
        LeagueSession session = getActiveSessionByUserId(userId);
        return session.getUsers().stream()
                .filter(up -> up.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Pontuação não encontrada para o usuário na sessão ativa"));
    }

    /**
     * Obtém o UserPunctuation da sessão de liga ativa de um usuário por email
     * @param email o email do usuário
     * @return o UserPunctuation em forma de {@link UserPunctuation}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public UserPunctuation getActivePunctuationByUserEmail(String email) {
        if (userService.existsByEmail(email)) {
            Long userId = userService.getObjectByEmail(email).getId();
            return getActivePunctuationByUserId(userId);
        }
        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Obtém a response do UserPunctuation da sessão de liga ativa de um usuário por ID
     * @param userId o ID do usuário
     * @return o UserPunctuation em forma da DTO {@link UserPunctuationResponseDTO}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public ResponseDTO getActivePunctuationResponseByUserId(Long userId) {
        UserPunctuation punctuation = getActivePunctuationByUserId(userId);
        return new UserPunctuationResponseDTO(punctuation);
    }

    /**
     * Obtém a response do UserPunctuation da sessão de liga ativa de um usuário por email
     * @param email o email do usuário
     * @return o UserPunctuation em forma da DTO {@link UserPunctuationResponseDTO}
     * @throws NotFoundException quando não houver sessão ativa para o usuário
     */
    @Transactional(readOnly = true)
    public ResponseDTO getActivePunctuationResponseByUserEmail(String email) {
        UserPunctuation punctuation = getActivePunctuationByUserEmail(email);
        return new UserPunctuationResponseDTO(punctuation);
    }
}
