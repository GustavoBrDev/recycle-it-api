package com.ifsc.ctds.stinghen.recycle_it_api.models.goals;

public interface IItem {

    void increment();
    void decrement();
    
    /**
     * Calcula o progresso do item com base na quantidade atual e alvo
     * @return progresso calculado como valor entre 0.0 e 1.0
     */
    default float calculateProgress() {
        // Implementação padrão - deve ser sobrescrito nas classes concretas
        return 0.0f;
    }
}
