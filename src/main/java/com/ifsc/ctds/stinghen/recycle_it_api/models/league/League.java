package com.ifsc.ctds.stinghen.recycle_it_api.models.league;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class League {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int tier;

    private int membersCount;
    private int promotedCount;
    private int relegatedCount;

    private boolean promotionEnabled;
    private boolean relegationEnabled;

}
