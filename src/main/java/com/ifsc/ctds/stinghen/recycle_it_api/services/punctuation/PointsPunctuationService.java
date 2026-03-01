package com.ifsc.ctds.stinghen.recycle_it_api.services.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation.PointsPunctuationResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.BadValueException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.pontuation.PointsPunctuationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de pontuações por pontos
 * @author Gustavo Stinghen
 * @see PointsPunctuation
 * @since 25/02/2026
 */
@AllArgsConstructor
@Service
public class PointsPunctuationService {

    public PointsPunctuationRepository repository;

    /**
     * Cria/persiste o registro de uma pontuação por pontos no banco de dados
     * @param user o usuário dono da pontuação
     */
    @Transactional
    public void create(User user) {

        PointsPunctuation punctuation = PointsPunctuation.builder()
                .knowledgePoints(0L)
                .recyclePoints(0L)
                .reducePoints(0L)
                .reusePoints(0L)
                .user(user)
                .build();

        repository.save(punctuation);
    }

    /**
     * Atualiza os pontos de redução de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param reducePoints os novos pontos de redução (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editReducePoints(Long id, Long reducePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (reducePoints < 0) {
                throw new BadValueException("O valor de pontos de redução não pode ser negativo");
            }
            
            pointsPunctuation.setReducePoints(reducePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Atualiza os pontos de redução da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param reducePoints os novos pontos de redução (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editReducePointsByUserId(Long userId, Long reducePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (reducePoints < 0) {
            throw new BadValueException("O valor de pontos de redução não pode ser negativo");
        }
        
        pointsPunctuation.setReducePoints(reducePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de redução da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param reducePoints os novos pontos de redução (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editReducePointsByUserEmail(String email, Long reducePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (reducePoints < 0) {
            throw new BadValueException("O valor de pontos de redução não pode ser negativo");
        }
        
        pointsPunctuation.setReducePoints(reducePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de redução de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO incrementReducePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setReducePoints(pointsPunctuation.getReducePoints() + amount);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Incrementa os pontos de redução da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementReducePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        pointsPunctuation.setReducePoints(pointsPunctuation.getReducePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de redução da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementReducePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        pointsPunctuation.setReducePoints(pointsPunctuation.getReducePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de redução de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO decrementReducePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (pointsPunctuation.getReducePoints() < amount) {
                pointsPunctuation.setReducePoints(0L);
                repository.save(pointsPunctuation);
                throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
            } else {
                pointsPunctuation.setReducePoints(pointsPunctuation.getReducePoints() - amount);
            }
            
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Decrementa os pontos de redução da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementReducePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (pointsPunctuation.getReducePoints() < amount) {
            pointsPunctuation.setReducePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setReducePoints(pointsPunctuation.getReducePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de redução da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementReducePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (pointsPunctuation.getReducePoints() < amount) {
            pointsPunctuation.setReducePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setReducePoints(pointsPunctuation.getReducePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de reciclagem de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param recyclePoints os novos pontos de reciclagem (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editRecyclePoints(Long id, Long recyclePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (recyclePoints < 0) {
                throw new BadValueException("O valor de pontos de reciclagem não pode ser negativo");
            }
            
            pointsPunctuation.setRecyclePoints(recyclePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Atualiza os pontos de reciclagem da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param recyclePoints os novos pontos de reciclagem (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editRecyclePointsByUserId(Long userId, Long recyclePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (recyclePoints < 0) {
            throw new BadValueException("O valor de pontos de reciclagem não pode ser negativo");
        }
        
        pointsPunctuation.setRecyclePoints(recyclePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de reciclagem da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param recyclePoints os novos pontos de reciclagem (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editRecyclePointsByUserEmail(String email, Long recyclePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (recyclePoints < 0) {
            throw new BadValueException("O valor de pontos de reciclagem não pode ser negativo");
        }
        
        pointsPunctuation.setRecyclePoints(recyclePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de reciclagem de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO incrementRecyclePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setRecyclePoints(pointsPunctuation.getRecyclePoints() + amount);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Incrementa os pontos de reciclagem da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementRecyclePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        pointsPunctuation.setRecyclePoints(pointsPunctuation.getRecyclePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de reciclagem da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementRecyclePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        pointsPunctuation.setRecyclePoints(pointsPunctuation.getRecyclePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de reciclagem de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO decrementRecyclePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (pointsPunctuation.getRecyclePoints() < amount) {
                pointsPunctuation.setRecyclePoints(0L);
                repository.save(pointsPunctuation);
                throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
            } else {
                pointsPunctuation.setRecyclePoints(pointsPunctuation.getRecyclePoints() - amount);
            }
            
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Decrementa os pontos de reciclagem da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementRecyclePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (pointsPunctuation.getRecyclePoints() < amount) {
            pointsPunctuation.setRecyclePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setRecyclePoints(pointsPunctuation.getRecyclePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de reciclagem da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementRecyclePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (pointsPunctuation.getRecyclePoints() < amount) {
            pointsPunctuation.setRecyclePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setRecyclePoints(pointsPunctuation.getRecyclePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de reuso de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param reusePoints os novos pontos de reuso (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editReusePoints(Long id, Long reusePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (reusePoints < 0) {
                throw new BadValueException("O valor de pontos de reuso não pode ser negativo");
            }
            
            pointsPunctuation.setReusePoints(reusePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Atualiza os pontos de reuso da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param reusePoints os novos pontos de reuso (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editReusePointsByUserId(Long userId, Long reusePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (reusePoints < 0) {
            throw new BadValueException("O valor de pontos de reuso não pode ser negativo");
        }
        
        pointsPunctuation.setReusePoints(reusePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de reuso da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param reusePoints os novos pontos de reuso (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editReusePointsByUserEmail(String email, Long reusePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (reusePoints < 0) {
            throw new BadValueException("O valor de pontos de reuso não pode ser negativo");
        }
        
        pointsPunctuation.setReusePoints(reusePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de reuso de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO incrementReusePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setReusePoints(pointsPunctuation.getReusePoints() + amount);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Incrementa os pontos de reuso da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementReusePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        pointsPunctuation.setReusePoints(pointsPunctuation.getReusePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de reuso da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementReusePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        pointsPunctuation.setReusePoints(pointsPunctuation.getReusePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de reuso de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO decrementReusePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (pointsPunctuation.getReusePoints() < amount) {
                pointsPunctuation.setReusePoints(0L);
                repository.save(pointsPunctuation);
                throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
            } else {
                pointsPunctuation.setReusePoints(pointsPunctuation.getReusePoints() - amount);
            }
            
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Decrementa os pontos de reuso da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementReusePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (pointsPunctuation.getReusePoints() < amount) {
            pointsPunctuation.setReusePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setReusePoints(pointsPunctuation.getReusePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de reuso da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementReusePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (pointsPunctuation.getReusePoints() < amount) {
            pointsPunctuation.setReusePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setReusePoints(pointsPunctuation.getReusePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de conhecimento de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param knowledgePoints os novos pontos de conhecimento (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editKnowledgePoints(Long id, Long knowledgePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (knowledgePoints < 0) {
                throw new BadValueException("O valor de pontos de conhecimento não pode ser negativo");
            }
            
            pointsPunctuation.setKnowledgePoints(knowledgePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Atualiza os pontos de conhecimento da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param knowledgePoints os novos pontos de conhecimento (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editKnowledgePointsByUserId(Long userId, Long knowledgePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (knowledgePoints < 0) {
            throw new BadValueException("O valor de pontos de conhecimento não pode ser negativo");
        }
        
        pointsPunctuation.setKnowledgePoints(knowledgePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de conhecimento da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param knowledgePoints os novos pontos de conhecimento (não pode ser negativo)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     * @throws BadValueException quando o valor for negativo
     */
    @Transactional
    public ResponseDTO editKnowledgePointsByUserEmail(String email, Long knowledgePoints) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (knowledgePoints < 0) {
            throw new BadValueException("O valor de pontos de conhecimento não pode ser negativo");
        }
        
        pointsPunctuation.setKnowledgePoints(knowledgePoints);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de conhecimento de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO incrementKnowledgePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setKnowledgePoints(pointsPunctuation.getKnowledgePoints() + amount);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Incrementa os pontos de conhecimento da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementKnowledgePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        pointsPunctuation.setKnowledgePoints(pointsPunctuation.getKnowledgePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Incrementa os pontos de conhecimento da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO incrementKnowledgePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        pointsPunctuation.setKnowledgePoints(pointsPunctuation.getKnowledgePoints() + amount);
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de conhecimento de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO decrementKnowledgePoints(Long id, Long amount) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            
            if (pointsPunctuation.getKnowledgePoints() < amount) {
                pointsPunctuation.setKnowledgePoints(0L);
                repository.save(pointsPunctuation);
                throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
            } else {
                pointsPunctuation.setKnowledgePoints(pointsPunctuation.getKnowledgePoints() - amount);
            }
            
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

    /**
     * Decrementa os pontos de conhecimento da pontuação mais recente do usuário pelo ID
     * @param userId o ID do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementKnowledgePointsByUserId(Long userId, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        
        if (pointsPunctuation.getKnowledgePoints() < amount) {
            pointsPunctuation.setKnowledgePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setKnowledgePoints(pointsPunctuation.getKnowledgePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Decrementa os pontos de conhecimento da pontuação mais recente do usuário pelo e-mail
     * @param email o e-mail do usuário
     * @param amount a quantidade de pontos a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional
    public ResponseDTO decrementKnowledgePointsByUserEmail(String email, Long amount) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        
        if (pointsPunctuation.getKnowledgePoints() < amount) {
            pointsPunctuation.setKnowledgePoints(0L);
            repository.save(pointsPunctuation);
            throw new BadValueException("A pontuação por pontos não tem saldo suficiente");
        } else {
            pointsPunctuation.setKnowledgePoints(pointsPunctuation.getKnowledgePoints() - amount);
        }
        
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Obtem o objeto de PointsPunctuation pelo id fornecido
     * @param id o id a ser buscado
     * @return a pontuação por pontos em forma de {@link PointsPunctuation}
     */
    @Transactional(readOnly = true)
    public PointsPunctuation getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todas as pontuações por pontos
     * @return lista de pontuações por pontos em forma de {@link PointsPunctuation}
     */
    @Transactional(readOnly = true)
    public List<PointsPunctuation> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as pontuações por pontos de forma paginada
     * @param pageable as configurações de paginação
     * @return página de pontuações por pontos em forma de {@link PointsPunctuation} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PointsPunctuation> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém pontuações por pontos pelo ID do usuário
     * @param userId o ID do usuário
     * @return lista de pontuações por pontos em forma de {@link PointsPunctuation}
     */
    @Transactional(readOnly = true)
    public List<PointsPunctuation> getObjectByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Obtém pontuações por pontos pelo email do usuário
     * @param email o email do usuário
     * @return lista de pontuações por pontos em forma de {@link PointsPunctuation}
     */
    @Transactional(readOnly = true)
    public List<PointsPunctuation> getObjectByUserEmail(String email) {
        return repository.findByUserCredentialEmail(email);
    }

    /**
     * Obtém pontuações por pontos pelo ID do usuário como ResponseDTO
     * @param userId o ID do usuário
     * @return lista de pontuações por pontos em forma de {@link PointsPunctuationResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<PointsPunctuationResponseDTO> getResponseByUserId(Long userId) {
        List<PointsPunctuation> pointsPunctuations = repository.findByUserId(userId);
        return pointsPunctuations.stream()
                .map(PointsPunctuationResponseDTO::new)
                .toList();
    }

    /**
     * Obtém pontuações por pontos pelo email do usuário como ResponseDTO
     * @param email o email do usuário
     * @return lista de pontuações por pontos em forma de {@link PointsPunctuationResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<PointsPunctuationResponseDTO> getResponseByUserEmail(String email) {
        List<PointsPunctuation> pointsPunctuations = repository.findByUserCredentialEmail(email);
        return pointsPunctuations.stream()
                .map(PointsPunctuationResponseDTO::new)
                .toList();
    }

    /**
     * Obtém a pontuação mais recente pelo ID do usuário
     * @param userId o ID do usuário
     * @return a pontuação mais recente em forma de {@link PointsPunctuation}
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional(readOnly = true)
    public PointsPunctuation getMostRecentObjectByUserId(Long userId) {
        return repository.findMostRecentByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Nenhuma pontuação por pontos encontrada para o usuário com id: " + userId
                ));
    }

    /**
     * Obtém a pontuação mais recente pelo email do usuário
     * @param email o email do usuário
     * @return a pontuação mais recente em forma de {@link PointsPunctuation}
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional(readOnly = true)
    public PointsPunctuation getMostRecentObjectByUserEmail(String email) {
        return repository.findMostRecentByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Nenhuma pontuação por pontos encontrada para o usuário com email: " + email
                ));
    }

    /**
     * Obtém a pontuação mais recente pelo ID do usuário como ResponseDTO
     * @param userId o ID do usuário
     * @return a pontuação mais recente em forma de {@link PointsPunctuationResponseDTO}
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional(readOnly = true)
    public PointsPunctuationResponseDTO getMostRecentResponseByUserId(Long userId) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserId(userId);
        return new PointsPunctuationResponseDTO(pointsPunctuation);
    }

    /**
     * Obtém a pontuação mais recente pelo email do usuário como ResponseDTO
     * @param email o email do usuário
     * @return a pontuação mais recente em forma de {@link PointsPunctuationResponseDTO}
     * @throws EntityNotFoundException quando nenhuma pontuação for encontrada para o usuário
     */
    @Transactional(readOnly = true)
    public PointsPunctuationResponseDTO getMostRecentResponseByUserEmail(String email) {
        PointsPunctuation pointsPunctuation = getMostRecentObjectByUserEmail(email);
        return new PointsPunctuationResponseDTO(pointsPunctuation);
    }

    /**
     * Deleta a pontuação por pontos com base em seu ID
     * @param id o id da pontuação por pontos a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos deletada")
                    .content("A pontuação por pontos com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }

}
