package com.ifsc.ctds.stinghen.recycle_it_api.models.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private String description;

    @OneToMany
    private List<ProjectMaterial> materials;

    private String instructions;

    @OneToMany
    private List<UserComment> userComments;
}
