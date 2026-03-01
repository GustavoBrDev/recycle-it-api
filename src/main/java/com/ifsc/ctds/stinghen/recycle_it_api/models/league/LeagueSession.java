package com.ifsc.ctds.stinghen.recycle_it_api.models.league;

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

    @ManyToOne
    private League league;

    @OneToMany(mappedBy = "session")
    private List<UserPunctuation> users;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isFinished;

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }
}
