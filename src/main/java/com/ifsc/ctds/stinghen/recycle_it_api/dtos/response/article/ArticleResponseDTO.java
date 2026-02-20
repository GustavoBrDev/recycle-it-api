package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;

/**
 * DTO de resposta abstrata (genérica) para os artigos
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class ArticleResponseDTO implements ResponseDTO {

    public Long id;

    public String title;

    public String description;

    public Duration minimumTime;
}
