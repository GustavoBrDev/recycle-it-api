package com.ifsc.ctds.stinghen.recycle_it_api.models.pontutation;

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
        return 0L;
    }
}
