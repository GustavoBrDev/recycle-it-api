package com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Punctuation implements IPunctuation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    public User user;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
