package com.ifsc.ctds.stinghen.recycle_it_api.models.article;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Duration;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public abstract class Article implements IArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Duration minimumTime;

    @ManyToOne
    private User author;
}
