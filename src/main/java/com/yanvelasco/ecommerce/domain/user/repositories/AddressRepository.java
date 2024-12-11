package com.yanvelasco.ecommerce.domain.user.repositories;

import com.yanvelasco.ecommerce.domain.user.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
}