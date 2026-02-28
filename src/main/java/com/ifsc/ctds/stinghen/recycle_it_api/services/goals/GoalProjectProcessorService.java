package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service utilitário para processar o impacto de projetos em metas
 * Centraliza a lógica de processamento para ser reutilizável em diferentes contextos
 * Pode ser utilizado por controllers, outras services ou endpoints
 * 
 * @author Gustavo Stinghen
 * @since 28/02/2026
 */
@AllArgsConstructor
@Service
public class GoalProjectProcessorService {

    public RecycleGoalService recycleGoalService;
    public ReduceGoalService reduceGoalService;

    /**
     * Processa o impacto completo de um projeto finalizado em todas as metas do usuário
     * Método principal que orquestra o processamento de metas de reciclagem e redução
     * 
     * @param userId ID do usuário que finalizou o projeto
     * @param project Projeto finalizado a ser processado
     * @return resultado do processamento com pontos e progresso atualizados
     */
    @Transactional
    public GoalProjectProcessResult processProjectCompletion(Long userId, Project project) {
        // Processa meta de reciclagem
        float recycleProgress = recycleGoalService.processProjectCompletion(userId, project);
        
        // Processa meta de redução e obtém pontos de reutilização
        int reusePoints = reduceGoalService.processProjectCompletion(userId, project);
        
        return GoalProjectProcessResult.builder()
                .userId(userId)
                .projectId(project.getId())
                .recycleProgress(recycleProgress)
                .reusePoints(reusePoints)
                .totalPoints(5L + reusePoints) // 5 pontos fixos + pontos de reutilização
                .processedAt(java.time.LocalDateTime.now())
                .build();
    }

    /**
     * Processa apenas o impacto na meta de reciclagem
     * Utilitário para contextos específicos onde apenas a meta de reciclagem é relevante
     * 
     * @param userId ID do usuário
     * @param project Projeto finalizado
     * @return progresso atualizado da meta de reciclagem
     */
    @Transactional
    public float processRecycleGoalOnly(Long userId, Project project) {
        return recycleGoalService.processProjectCompletion(userId, project);
    }

    /**
     * Processa apenas o impacto na meta de redução
     * Utilitário para contextos específicos onde apenas a meta de redução é relevante
     * 
     * @param userId ID do usuário
     * @param project Projeto finalizado
     * @return pontos de reutilização concedidos
     */
    @Transactional
    public int processReduceGoalOnly(Long userId, Project project) {
        return reduceGoalService.processProjectCompletion(userId, project);
    }

    /**
     * Verifica se um usuário tem metas ativas para processamento
     * Utilitário para validações pré-processamento
     * 
     * @param userId ID do usuário
     * @return informações sobre metas ativas do usuário
     */
    @Transactional(readOnly = true)
    public UserGoalsStatus getUserGoalsStatus(Long userId) {
        boolean hasActiveRecycleGoal = !recycleGoalService.getActiveByUserId(userId).isEmpty();
        boolean hasActiveReduceGoal = !reduceGoalService.getActiveByUserId(userId).isEmpty();
        
        return UserGoalsStatus.builder()
                .userId(userId)
                .hasActiveRecycleGoal(hasActiveRecycleGoal)
                .hasActiveReduceGoal(hasActiveReduceGoal)
                .hasAnyActiveGoal(hasActiveRecycleGoal || hasActiveReduceGoal)
                .build();
    }

    /**
     * DTO para representar o resultado do processamento de projeto
     */
    @lombok.Data
    @lombok.Builder
    public static class GoalProjectProcessResult {
        private Long userId;
        private Long projectId;
        private Float recycleProgress;
        private Integer reusePoints;
        private Long totalPoints;
        private java.time.LocalDateTime processedAt;
    }

    /**
     * DTO para representar o status de metas do usuário
     */
    @lombok.Data
    @lombok.Builder
    public static class UserGoalsStatus {
        private Long userId;
        private boolean hasActiveRecycleGoal;
        private boolean hasActiveReduceGoal;
        private boolean hasAnyActiveGoal;
    }
}
