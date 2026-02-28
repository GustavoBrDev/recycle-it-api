package com.ifsc.ctds.stinghen.recycle_it_api.models.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
public class ReduceItem implements IItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Materials type;

    private int targetQuantity;

    private int actualQuantity;

    @Override
    public void increment() {
        this.actualQuantity++;
    }

    @Override
    public void decrement() {
        this.actualQuantity--;
    }

    @Override
    public float calculateProgress() {
        if (targetQuantity <= 0) {
            return 0.0f;
        }
        
        // Para ReduceItem, o progresso é a porcentagem de redução alcançada
        // targetQuantity = consumo inicial (baseline)
        // actualQuantity = consumo atual (após redução)
        int reduction = targetQuantity - actualQuantity;
        
        if (reduction <= 0) {
            return 0.0f; // Não houve redução
        }
        
        // Calcula a porcentagem de redução alcançada
        float reductionPercentage = (float) reduction / targetQuantity;
        
        // Garante que o progresso mínimo seja 0.0
        return Math.max(0.0f, reductionPercentage);
    }
}
