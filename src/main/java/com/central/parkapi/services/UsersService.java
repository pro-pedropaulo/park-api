package com.central.parkapi.services;

import com.central.parkapi.Dtos.UserAlterPasswordDTO;
import com.central.parkapi.entity.User;
import com.central.parkapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }

    @Transactional
    public String updatePassword(Long id, UserAlterPasswordDTO userAlterPasswordDTO) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        if(!userAlterPasswordDTO.getActualPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if(!userAlterPasswordDTO.getNewPassword().equals(userAlterPasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if(userAlterPasswordDTO.getNewPassword().equals(user.getPassword())) {
            throw new RuntimeException("New password must be different from the current password");
        }

        user.setPassword(userAlterPasswordDTO.getNewPassword());
        userRepository.save(user);
        return "Password updated successfully";
    }

    @Transactional
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
