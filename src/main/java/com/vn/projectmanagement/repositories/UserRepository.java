package com.vn.projectmanagement.repositories;

import com.vn.projectmanagement.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    @Query("SELECT user FROM User user " +
            "GROUP BY user.id, user.username, user.email, user.phone, user.role"
    )
    Page<User> listUser(Pageable pageable);
}
