package com.serezka.database.authorization.service;

import com.serezka.database.authorization.model.User;
import com.serezka.database.authorization.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.1
 * <p>
 * Service for user entity
 * @see UserRepository
 * @see User
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;

    /**
     * Save or update user
     * @param user - user to save
     * @return saved user entity with id
     * @see User
     */
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Find user by username
     * @param chatId - chat id to search
     * @return optional of user entity
     * @see User
     */
    @Transactional
    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    /**
     * Find user by username
     * @param username - username to search
     * @return optional of user entity
     * @see User
     */
    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find all users by role
     * @param role - role to search
     * @return list of user entities
     * @see User
     */
    @Transactional
    public List<User> findAllByRole(User.Role role) {
        return userRepository.findAllByRole(role);
    }
}
