package com.ifsc.ctds.stinghen.recycle_it_api.models.article;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class KnowledgeArticle extends Article {

    private String text;

    private List<String> article_references;

    @Override
    public void onFinish(User user) {

    }
}
