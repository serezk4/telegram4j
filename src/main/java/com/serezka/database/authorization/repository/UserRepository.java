package com.serezka.database.authorization.repository;

import com.serezka.database.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.1
 * <p>
 * Repository for user entity
 * @see User
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);
    Optional<User> findByUsername(String username);

    List<User> findAllByRole(User.Role role);
}
