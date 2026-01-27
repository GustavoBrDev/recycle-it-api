package com.ifsc.ctds.stinghen.recycle_it_api.models.league;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.League;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class LeagueSession implements ILeagueSession{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private League league;

    @OneToMany
    private List<UserPunctuation> users;

    private LocalDate startDate;

    private LocalDate endDate;

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }
}
