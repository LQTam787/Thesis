package com.nutrition.ai.nutritionaibackend.service;

import com.nutrition.ai.nutritionaibackend.dto.UserDto;
import com.nutrition.ai.nutritionaibackend.model.domain.User;

import java.util.Optional;

/**
 * Service Interface for managing {@link User}.
 */
public interface UserService {

    /**
     * Find a user by username.
     *
     * @param username the username of the user.
     * @return an Optional containing the user, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a user exists by username.
     *
     * @param username the username to check.
     * @return true if the user exists, false otherwise.
     */
    Boolean existsByUsername(String username);

    /**
     * Check if a user exists by email.
     *
     * @param email the email to check.
     * @return true if the user exists, false otherwise.
     */
    Boolean existsByEmail(String email);

    /**
     * Save a user.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    User save(User user);

    /**
     * Authenticates a user.
     *
     * @param username the username.
     * @param password the password.
     * @return true if authentication is successful, false otherwise.
     */
    boolean authenticate(String username, String password);

    /**
     * Registers a new user account.
     *
     * @param userDto the user data transfer object.
     * @return the created user.
     */
    UserDto registerNewUserAccount(UserDto userDto) throws Exception;
}
