package com.php.design_patten_demo.repository;

import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(UserRole role);
    boolean existsByUsername(String username);
}
