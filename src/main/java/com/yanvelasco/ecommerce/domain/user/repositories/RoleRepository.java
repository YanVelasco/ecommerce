package com.yanvelasco.ecommerce.domain.user.repositories;

import com.yanvelasco.ecommerce.domain.user.entities.ModelRole;
import com.yanvelasco.ecommerce.domain.user.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByRoleName(ModelRole roleName);
}
