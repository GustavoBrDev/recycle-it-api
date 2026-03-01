package com.ifsc.ctds.stinghen.recycle_it_api.security.repository;

import com.ifsc.ctds.stinghen.recycle_it_api.security.models.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByEmail(String email);
    boolean existsByEmail(String email);
}
