package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.article;

import com.ifsc.ctds.stinghen.recycle_it_api.models.article.KnowledgeArticle;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de request para artigos de conhecimento
 */
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeArticleRequestDTO extends ArticleRequestDTO<KnowledgeArticle> {

    @NotBlank
    public String text;

    public List<String> references;

    @Override
    public KnowledgeArticle convert() {
        return KnowledgeArticle.builder()
                .title(super.title)
                .description(super.description)
                .minimumTime(super.minimumTime)
                .text(this.text)
                .references(this.references)
                .build();
    }
}
