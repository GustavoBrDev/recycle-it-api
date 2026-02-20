package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article;

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
}
