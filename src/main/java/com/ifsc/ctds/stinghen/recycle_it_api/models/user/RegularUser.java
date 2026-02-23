package com.ifsc.ctds.stinghen.recycle_it_api.models.user;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.InvalidRelationshipException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.Goal;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasedItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
public class RegularUser extends User{

    private String name;

    @ManyToMany
    private List<RegularUser> friends;

    @OneToMany(mappedBy = "user")
    private List<Punctuation> punctuations;

    private Avatar currentAvatar;

    @OneToMany(mappedBy = "user")
    private List<PurchasedItem> purchasedItems;

    private Long recycleGems;

    @ManyToOne
    private League actualLeague;

    @ManyToMany
    private List<Project> projects;

    @OneToMany(mappedBy = "user")
    private List<Goal> actualGoals;

    @OneToMany(mappedBy = "user")
    private List<Goal> nextGoals;

    /**
     * Adiciona um usuário a lista de amizade
     * @param friend o usuário a ser adicionado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    public void addFriend (RegularUser friend) {

        if (this.equals(friend)) {
            throw new InvalidRelationshipException("Você não pode adicionar a si mesmo");
        }

        if (this.friends.contains(friend)) {
            throw new InvalidRelationshipException("O usuário já é o seu amigo");
        }

        this.friends.add(friend);
        friend.friends.add(this);
    }

    /**
     * Remove um usuário da lista de amizade
     * @param friend o usuário a ser removido
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    public void removeFriend (RegularUser friend) {

        if (this.equals(friend)) {
            throw new InvalidRelationshipException("Você não pode remover a si mesmo");
        }

        if (! this.friends.contains(friend)) {
            throw new InvalidRelationshipException("O usuário não é o seu amigo");
        }

        this.friends.remove(friend);
        friend.friends.remove(this);
    }

    /**
     * Adiciona um projeto a lista de projetos em andamento
     * @param project o projeto a ser adicionado
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    public void addProject (Project project) {

        if (this.projects.contains(project)) {
            throw new InvalidRelationshipException("O projeto já está em andamento");
        }

        this.projects.add(project);
    }

    /**
     * Remove um projeto da lista de projetos em andamento
     * @param project o projeto a ser removido
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    public void removeProject (Project project) {

        if (! this.projects.contains(project)) {
            throw new InvalidRelationshipException("O projeto não está em andamento");
        }

        this.projects.remove(project);
    }
}
