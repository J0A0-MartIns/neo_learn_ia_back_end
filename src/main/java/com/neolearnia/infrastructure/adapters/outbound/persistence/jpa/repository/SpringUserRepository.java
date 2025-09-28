package com.neolearnia.infrastructure.adapters.outbound.persistence.jpa.repository;

import com.neolearnia.infrastructure.adapters.outbound.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringUserRepository extends JpaRepository<UserEntity, UUID> {

}
