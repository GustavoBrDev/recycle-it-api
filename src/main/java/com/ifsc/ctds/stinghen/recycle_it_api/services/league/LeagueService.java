package com.ifsc.ctds.stinghen.recycle_it_api.services.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.league.LeagueRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.FullLeagueResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.SimpleLeagueResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.league.LeagueRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para os métodos específicos da liga
 * @author Gustavo Stinghen
 * @see League
 * @since 22/02/2026
 */
@RequiredArgsConstructor
@Service
public class LeagueService {

    @Autowired
    public LeagueRepository repository;

    @Autowired
    @Lazy
    public RegularUserService userService;

    /**
     * Cria/persiste o registro de uma liga no banco de dados
     * @param dto a liga a ser criada em formato {@link LeagueRequestDTO}
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(LeagueRequestDTO dto) {
        repository.save(dto.convert());

        return FeedbackResponseDTO.builder()
                .mainMessage("Liga criada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza uma liga existente
     * @param id o id da liga
     * @param league a liga com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, League league) {
        League existingLeague = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Liga não encontrada com id: " + id
                ));

        existingLeague.setName(league.getName());
        existingLeague.setTier(league.getTier());
        existingLeague.setMembersCount(league.getMembersCount());
        existingLeague.setPromotedCount(league.getPromotedCount());
        existingLeague.setRelegatedCount(league.getRelegatedCount());
        existingLeague.setPromotionEnabled(league.isPromotionEnabled());
        existingLeague.setRelegationEnabled(league.isRelegationEnabled());

        repository.save(existingLeague);

        return FeedbackResponseDTO.builder()
                .mainMessage("Liga atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o nome de uma liga
     * @param id o id da liga
     * @param name o novo nome
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO editName(Long id, String name) {
        if (repository.existsById(id)) {
            League league = repository.findById(id).get();
            league.setName(name);
            repository.save(league);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com id: " + id);
    }

    /**
     * Atualiza o tier de uma liga
     * @param id o id da liga
     * @param tier o novo tier
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO editTier(Long id, int tier) {
        if (repository.existsById(id)) {
            League league = repository.findById(id).get();
            league.setTier(tier);
            repository.save(league);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com id: " + id);
    }

    /**
     * Atualiza a quantidade de membros de uma liga
     * @param id o id da liga
     * @param membersCount a nova quantidade de membros
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO editMembersCount(Long id, int membersCount) {
        if (repository.existsById(id)) {
            League league = repository.findById(id).get();
            league.setMembersCount(membersCount);
            repository.save(league);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com id: " + id);
    }

    /**
     * Atualiza a quantidade de promovidos de uma liga
     * @param id o id da liga
     * @param promotedCount a nova quantidade de promovidos
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO editPromotedCount(Long id, int promotedCount) {
        if (repository.existsById(id)) {
            League league = repository.findById(id).get();
            league.setPromotedCount(promotedCount);
            repository.save(league);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com id: " + id);
    }

    /**
     * Atualiza a quantidade de rebaixados de uma liga
     * @param id o id da liga
     * @param relegatedCount a nova quantidade de rebaixados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO editRelegatedCount(Long id, int relegatedCount) {
        if (repository.existsById(id)) {
            League league = repository.findById(id).get();
            league.setRelegatedCount(relegatedCount);
            repository.save(league);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com id: " + id);
    }

    /**
     * Atualiza o status de promoção de uma liga
     * @param id o id da liga
     * @param promotionEnabled o novo status de promoção
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO editPromotionEnabled(Long id, boolean promotionEnabled) {
        if (repository.existsById(id)) {
            League league = repository.findById(id).get();
            league.setPromotionEnabled(promotionEnabled);
            repository.save(league);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com id: " + id);
    }

    /**
     * Atualiza o status de rebaixamento de uma liga
     * @param id o id da liga
     * @param relegationEnabled o novo status de rebaixamento
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO editRelegationEnabled(Long id, boolean relegationEnabled) {
        if (repository.existsById(id)) {
            League league = repository.findById(id).get();
            league.setRelegationEnabled(relegationEnabled);
            repository.save(league);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de Liga pelo id fornecido
     * @param id o id a ser buscado
     * @return a liga em forma de {@link League}
     */
    @Transactional(readOnly = true)
    public League getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response completa de uma liga pelo id fornecido
     * @param id o id a ser buscado
     * @return a liga em forma da DTO {@link FullLeagueResponseDTO}
     * @throws NotFoundException quando a liga não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getFullById(Long id) {
        if (repository.existsById(id)) {
            return new FullLeagueResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Liga não encontrada com o ID " + id);
    }

    /**
     * Obtém a response breve de uma liga pelo id fornecido
     * @param id o id a ser buscado
     * @return a liga em forma da DTO {@link SimpleLeagueResponseDTO}
     * @throws NotFoundException quando a liga não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getSimpleById(Long id) {
        if (repository.existsById(id)) {
            return new SimpleLeagueResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Liga não encontrada com o ID " + id);
    }

    /**
     * Obtém uma liga pelo tier fornecido
     * @param tier o tier a ser buscado
     * @return a liga em forma de {@link League}
     */
    @Transactional(readOnly = true)
    public League getObjectByTier(int tier) {
        return repository.findLeagueByTier(tier);
    }

    /**
     * Obtém a response completa de uma liga pelo tier fornecido
     * @param tier o tier a ser buscado
     * @return a liga em forma da DTO {@link FullLeagueResponseDTO}
     * @throws NotFoundException quando a liga não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getFullByTier(int tier) {
        League league = repository.findLeagueByTier(tier);
        if (league != null) {
            return new FullLeagueResponseDTO(league);
        }

        throw new NotFoundException("Liga não encontrada com o tier " + tier);
    }

    /**
     * Obtém a response breve de uma liga pelo tier fornecido
     * @param tier o tier a ser buscado
     * @return a liga em forma da DTO {@link SimpleLeagueResponseDTO}
     * @throws NotFoundException quando a liga não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getSimpleByTier(int tier) {
        League league = repository.findLeagueByTier(tier);
        if (league != null) {
            return new SimpleLeagueResponseDTO(league);
        }

        throw new NotFoundException("Liga não encontrada com o tier " + tier);
    }

    /**
     * Obtém todas as ligas
     * @return lista de ligas em forma de {@link League}
     */
    @Transactional(readOnly = true)
    public List<League> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as ligas de forma simplificada
     * @return lista de ligas em forma de {@link SimpleLeagueResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<SimpleLeagueResponseDTO> getAllSimple() {
        return repository.findAll().stream().map(SimpleLeagueResponseDTO::new).toList();
    }

    /**
     * Obtém todas as ligas de forma completa
     * @return lista de ligas em forma de {@link FullLeagueResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<FullLeagueResponseDTO> getAllFull() {
        return repository.findAll().stream().map(FullLeagueResponseDTO::new).toList();
    }

    /**
     * Obtém todas as ligas de forma paginada
     * @param pageable as configurações de paginação
     * @return página de ligas em forma de {@link League} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<League> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém todas as ligas de forma simplificada e paginada
     * @param pageable as configurações de paginação
     * @return página de ligas em forma de {@link SimpleLeagueResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<SimpleLeagueResponseDTO> getAllSimple(Pageable pageable) {
        return repository.findAll(pageable).map(SimpleLeagueResponseDTO::new);
    }

    /**
     * Obtém todas as ligas de forma completa e paginada
     * @param pageable as configurações de paginação
     * @return página de ligas em forma de {@link FullLeagueResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<FullLeagueResponseDTO> getAllFull(Pageable pageable) {
        return repository.findAll(pageable).map(FullLeagueResponseDTO::new);
    }

    /**
     * Deleta a liga com base em seu ID
     * @param id o id da liga a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a liga não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Liga deletada")
                    .content("A liga com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Liga não encontrada com o ID " + id);
    }
}
