package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * DTO abstrata para artigos
 * @param <T> o tipo do artigo (Generics)
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class ArticleRequestDTO<T> implements ConvertibleRequestDTO<T> {

    @NotBlank
    public String title;

    @NotBlank
    public String description;

    @NotNull
    public Duration minimumTime;
}
