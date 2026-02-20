package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation;

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

}
