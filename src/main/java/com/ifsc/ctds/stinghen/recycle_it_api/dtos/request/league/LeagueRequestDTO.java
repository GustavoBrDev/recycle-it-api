package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class LeagueRequestDTO implements ConvertibleRequestDTO<League> {


    @NotBlank
    public String name;

    @NotNull
    @PositiveOrZero
    public int tier;

    @Positive
    @NotNull
    public int membersCount;

    @PositiveOrZero
    @NotNull
    public int promotedCount;

    @PositiveOrZero
    @NotNull
    public int relegatedCount;

    @NotNull
    public boolean promotionEnabled;

    @NotNull
    public boolean relegationEnabled;

    @Override
    public League convert() {
        return League.builder()
                .name(this.name)
                .tier(this.tier)
                .membersCount(this.membersCount)
                .promotedCount(this.promotedCount)
                .relegatedCount(this.relegatedCount)
                .promotionEnabled(this.promotionEnabled)
                .relegationEnabled(this.relegationEnabled)
                .build();
    }
}
