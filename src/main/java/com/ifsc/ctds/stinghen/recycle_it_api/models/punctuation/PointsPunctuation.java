package com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
public class PointsPunctuation extends Punctuation {

    private Long reducePoints;
    private Long recyclePoints;
    private Long reusePoints;
    private Long knowledgePoints;


    @Override
    public Long calculateTotal() {

        double total =
                (recyclePoints != null ? recyclePoints : 0L)
                        + (reusePoints != null ? reusePoints : 0L)
                        + (knowledgePoints != null ? knowledgePoints : 0L) * 0.85
                        + (reducePoints != null ? reducePoints : 0L) * 0.15;

        return Math.round(total);
    }
}
