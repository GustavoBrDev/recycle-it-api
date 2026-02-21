package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article;

import com.ifsc.ctds.stinghen.recycle_it_api.models.article.KnowledgeArticle;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * DTO de resposta concreta para os artigos de conhecimento
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class KnowledgeArticleResponse extends ArticleResponseDTO {

    public String text;

    public List<String> references;

    public KnowledgeArticleResponse(KnowledgeArticle article) {
        super(article);
        this.text = article.getText();
        this.references = article.getArticle_references();
    }
}
