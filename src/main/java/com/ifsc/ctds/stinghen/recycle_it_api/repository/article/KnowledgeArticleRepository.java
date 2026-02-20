package com.ifsc.ctds.stinghen.recycle_it_api.repository.article;


import com.ifsc.ctds.stinghen.recycle_it_api.models.article.KnowledgeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositório para a entidade de Artigos de Conhecimento
 * Com specification executor para filtros complexos
 */
public interface KnowledgeArticleRepository extends JpaRepository<KnowledgeArticle, Long>, JpaSpecificationExecutor<KnowledgeArticle> {
}
