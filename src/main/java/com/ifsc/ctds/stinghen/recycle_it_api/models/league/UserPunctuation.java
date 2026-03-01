package com.ifsc.ctds.stinghen.recycle_it_api.models.league;

import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class UserPunctuation implements ILeaguePunctuation{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private LeagueSession session;

    @OneToOne
    private PointsPunctuation punctuation;

    @Override
    public Long calculateTotal() {

        Long recyclePoints = punctuation.getRecyclePoints();
        Long reusePoints = punctuation.getReusePoints();
        Long knowledgePoints = punctuation.getKnowledgePoints();
        Long reducePoints = punctuation.getReducePoints();

        double total =
                (recyclePoints != null ? recyclePoints : 0L)
                        + (reusePoints != null ? reusePoints : 0L)
                        + (knowledgePoints != null ? knowledgePoints : 0L) * 0.85
                        + (reducePoints != null ? reducePoints : 0L) * 0.15;

        return Math.round(total);
    }
}
