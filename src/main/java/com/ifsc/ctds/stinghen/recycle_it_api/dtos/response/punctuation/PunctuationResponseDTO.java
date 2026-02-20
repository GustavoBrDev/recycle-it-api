package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO de retorno da pontuação de forma abstrata (genérica)
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class PunctuationResponseDTO implements ResponseDTO {

    public Long id;

    public LocalDateTime lastUpdated;
}
