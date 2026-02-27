package com.ifsc.ctds.stinghen.recycle_it_api.specifications;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe de Specifications para filtros complexos em Project
 * @author Gustavo Stinghen
 */
public class ProjectSpecification {

    /**
     * Cria uma Specification para filtros complexos de Project
     * @param search termo de busca inteligente
     * @return Specification configurada
     */
    public static Specification<Project> getFiltered(String search) {
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

                // Comparação com o campo "text"
                orPredicates.add(cb.like(
                        cb.lower(root.get("text")), "%" + search.toLowerCase() + "%"
                ));

                // Comparação com o campo "description"
                orPredicates.add(cb.like(
                        cb.lower(root.get("description")), "%" + search.toLowerCase() + "%"
                ));

                // Comparação com o campo "instructions"
                orPredicates.add(cb.like(
                        cb.lower(root.get("instructions")), "%" + search.toLowerCase() + "%"
                ));

                // Adiciona os predicados de OR ao conjunto principal
                predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
            }

            // Combina todos os filtros com AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Cria uma Specification para recomendar projetos com base nos materiais do usuário
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @return Specification configurada para recomendações
     */
    public static Specification<Project> getRecommendedByMaterials(Map<Materials, Long> userMaterials) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userMaterials != null && !userMaterials.isEmpty()) {
                // Join com os materiais do projeto
                Join<Project, RecycledMaterial> materialsJoin = root.join("materials");

                // Cria predicados para cada tipo de material que o usuário possui
                List<Predicate> materialPredicates = new ArrayList<>();
                
                for (Map.Entry<Materials, Long> userMaterial : userMaterials.entrySet()) {
                    // Recomenda projetos que requerem quantidade menor ou igual à que o usuário possui
                    Predicate materialMatch = cb.and(
                        cb.equal(materialsJoin.get("type"), userMaterial.getKey()),
                        cb.lessThanOrEqualTo(materialsJoin.get("quantity"), userMaterial.getValue())
                    );
                    materialPredicates.add(materialMatch);
                }

                // Se o usuário tem materiais, recomenda projetos que usam qualquer um deles
                if (!materialPredicates.isEmpty()) {
                    predicates.add(cb.or(materialPredicates.toArray(new Predicate[0])));
                }
            } else {
                // Se usuário não tem materiais, recomenda projetos que não requerem materiais
                Join<Project, RecycledMaterial> materialsJoin = root.join("materials");
                predicates.add(cb.equal(materialsJoin.get("quantity"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Cria uma Specification combinada para busca inteligente e recomendação por materiais
     * @param search termo de busca inteligente
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @return Specification combinada
     */
    public static Specification<Project> getFilteredAndRecommended(String search, Map<Materials, Long> userMaterials) {
        Specification<Project> filteredSpec = getFiltered(search);
        Specification<Project> recommendedSpec = getRecommendedByMaterials(userMaterials);
        
        return filteredSpec.and(recommendedSpec);
    }

    /**
     * Cria uma Specification para projetos que o usuário pode completar com seus materiais atuais
     * Prioriza projetos que usam materiais similares em quantidade
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @param tolerancePercent percentual de tolerância para recomendação (ex: 20 para aceitar até 20% a mais)
     * @return Specification configurada
     */
    public static Specification<Project> getRecommendedByMaterialsWithTolerance(Map<Materials, Long> userMaterials, int tolerancePercent) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userMaterials != null && !userMaterials.isEmpty()) {
                // Join com os materiais do projeto
                Join<Project, RecycledMaterial> materialsJoin = root.join("materials");

                // Calcula o fator de tolerância
                double toleranceFactor = 1.0 + (tolerancePercent / 100.0);

                // Cria predicados para cada tipo de material que o usuário possui
                List<Predicate> materialPredicates = new ArrayList<>();
                
                for (Map.Entry<Materials, Long> userMaterial : userMaterials.entrySet()) {
                    // Recomenda projetos que requerem quantidade dentro da tolerância
                    long maxAcceptableQuantity = (long) (userMaterial.getValue() * toleranceFactor);
                    
                    Predicate materialMatch = cb.and(
                        cb.equal(materialsJoin.get("type"), userMaterial.getKey()),
                        cb.lessThanOrEqualTo(materialsJoin.get("quantity"), maxAcceptableQuantity)
                    );
                    materialPredicates.add(materialMatch);
                }

                // Se o usuário tem materiais, recomenda projetos que usam qualquer um deles
                if (!materialPredicates.isEmpty()) {
                    predicates.add(cb.or(materialPredicates.toArray(new Predicate[0])));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
