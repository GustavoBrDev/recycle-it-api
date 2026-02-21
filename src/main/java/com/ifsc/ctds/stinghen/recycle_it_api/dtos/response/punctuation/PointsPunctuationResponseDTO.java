package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO de retorno da pontuação medida via os pontos de redução, reciclagem, reutilização e conhecimento.
 * É a versão concreta da DTO de pontuação
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PointsPunctuationResponseDTO extends PunctuationResponseDTO {

    public Long reducePoints;

    public Long recyclePoints;

    public Long reusePoints;

    public Long knowledgePoints;

    public Long totalPoints;

    public PointsPunctuationResponseDTO(PointsPunctuation punctuation) {
        this.id = punctuation.getId();
        this.lastUpdated = punctuation.getLastUpdated();
        this.reducePoints = punctuation.getReducePoints();
        this.recyclePoints = punctuation.getRecyclePoints();
        this.reusePoints = punctuation.getReusePoints();
        this.knowledgePoints = punctuation.getKnowledgePoints();
        this.totalPoints = punctuation.calculateTotal();
    }

}
