package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request;

/**
 * Interface que representa um DTO de requisição convertível (para objeto).
 */
public interface ConvertibleRequestDTO<T> extends RequestDTO {

    T convert();
}
