package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.Article;
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
public class ArticleResponseDTO implements ResponseDTO {

    public Long id;

    public String title;

    public String description;

    public Duration minimumTime;

    public ArticleResponseDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.minimumTime = article.getMinimumTime();
    }
}
