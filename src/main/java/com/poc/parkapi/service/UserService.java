package com.poc.parkapi.service;

import com.poc.parkapi.entity.User;
import com.poc.parkapi.enums.Role;
import com.poc.parkapi.repository.UserRepository;
import com.poc.parkapi.web.exception.InvalidPasswordException;
import com.poc.parkapi.web.exception.UserNotFoundException;
import com.poc.parkapi.web.exception.UsernameUniqueViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' already registered", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s not founded", userId)));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username=%s not founded", username)));
    }

    @Transactional
    public void updatePassword(Long userId, String password, String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new InvalidPasswordException("The new password not match.");
        }

        User foundedUser = this.findById(userId);

        if (!passwordEncoder.matches(password, foundedUser.getPassword())) {
            throw new InvalidPasswordException("The actual password not match.");
        }

        foundedUser.setPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role findRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User role with username=%s not founded", username)));
    }
}
