package com.ifsc.ctds.stinghen.recycle_it_api.models.league;

import com.ifsc.ctds.stinghen.recycle_it_api.models.pontutation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany
    private List<Punctuation> punctuations;

    @Override
    public Long calculateTotal() {
        return 0L;
    }
}
