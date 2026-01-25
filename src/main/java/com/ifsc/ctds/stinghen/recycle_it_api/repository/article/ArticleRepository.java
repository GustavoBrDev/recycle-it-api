package com.ifsc.ctds.stinghen.recycle_it_api.repository.article;

import com.ifsc.ctds.stinghen.recycle_it_api.models.article.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



/**
 * Repositório para a entidade de Artigos
 * Com specification executor para filtros complexos
 */
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    Page<Article> findByAuthor_Id(Long id, Pageable pageable);

}
