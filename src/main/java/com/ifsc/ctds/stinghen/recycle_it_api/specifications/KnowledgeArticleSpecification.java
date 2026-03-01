package com.ifsc.ctds.stinghen.recycle_it_api.specifications;

import com.ifsc.ctds.stinghen.recycle_it_api.models.article.KnowledgeArticle;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Specifications para filtros complexos em KnowledgeArticle
 * @author Gustavo Stinghen
 */
public class KnowledgeArticleSpecification {

    /**
     * Cria uma Specification para filtros complexos de KnowledgeArticle
     * @param search termo de busca inteligente
     * @return Specification configurada
     */
    public static Specification<KnowledgeArticle> getFiltered(String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por search (busca inteligente)
            if (search != null && !search.isEmpty()) {
                List<Predicate> orPredicates = new ArrayList<>();

                try {
                    // Tentativa de converter o search para Long (caso seja um ID)
                    Long id = Long.parseLong(search);
                    orPredicates.add(cb.equal(root.get("id"), id));
                } catch (NumberFormatException ignored) {}

                // Comparação com o campo "title"
                orPredicates.add(cb.like(
                        cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"
                ));

                // Comparação com o campo "description"
                orPredicates.add(cb.like(
                        cb.lower(root.get("description")), "%" + search.toLowerCase() + "%"
                ));

                // Comparação com o campo "content"
                orPredicates.add(cb.like(
                        cb.lower(root.get("content")), "%" + search.toLowerCase() + "%"
                ));

                // Adiciona os predicados de OR ao conjunto principal
                predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
            }

            // Combina todos os filtros com AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
