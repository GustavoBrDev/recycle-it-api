package com.ifsc.ctds.stinghen.recycle_it_api.models.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper=true)
public class RecycledMaterial extends ProjectMaterial {

    private Materials type;
}
