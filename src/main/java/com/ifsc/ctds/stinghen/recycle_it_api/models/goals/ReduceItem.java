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

    private int quantity;

    @Override
    public void increment() {
        this.quantity++;
    }

    @Override
    public void decrement() {
        this.quantity--;
    }
}
