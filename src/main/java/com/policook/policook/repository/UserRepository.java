package com.policook.policook.repository;

import com.policook.policook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String name);
    boolean existsByEmail(String email);
}
