package com.group.identity_service.repository;

import com.group.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentityRepository extends JpaRepository<User,String> {
    public Optional<User> findByUserName(String userName);
}
