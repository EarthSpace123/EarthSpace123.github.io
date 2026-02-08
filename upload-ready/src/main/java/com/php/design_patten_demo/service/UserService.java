package com.php.design_patten_demo.service;

import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.enums.UserRole;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User findById(Long id);
    List<User> findByRole(UserRole role);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    boolean existsByUsername(String username);

    User recharge(Long userId, BigDecimal amount);
    User deductBalance(Long userId, BigDecimal amount);
}
