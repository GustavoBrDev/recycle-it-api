package com.ifsc.ctds.stinghen.recycle_it_api.models.user;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.Goal;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasedItem;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @OneToMany
    private List<FriendRequest> requests;

    @OneToMany
    private List<Punctuation> punctuations;

    private Avatar currentAvatar;

    @OneToMany
    private List<PurchasedItem> purchasedItems;

    private Long recycleGems;

    @ManyToOne
    private League actualLeague;

    @OneToMany
    private List<Project> projects;

    @OneToMany
    private List<Goal> actualGoals;

    @OneToMany
    private List<Goal> nextGoals;
}
