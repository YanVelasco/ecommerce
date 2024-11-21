package com.yanvelasco.ecommerce.domain.user.repositories;

import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserName(String userName);

    boolean existsByUserName(@NotBlank(message = "Username is required") String username);

    boolean existsByEmail(@NotBlank(message = "Email is required") String email);
}
