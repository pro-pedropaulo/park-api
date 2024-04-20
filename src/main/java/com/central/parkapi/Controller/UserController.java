package com.central.parkapi.Controller;

import com.central.parkapi.Dtos.UserAlterPasswordDTO;
import com.central.parkapi.Dtos.UserDTO;
import com.central.parkapi.Dtos.UserResponseDTO;
import com.central.parkapi.Mappers.UserMapper;
import com.central.parkapi.entity.User;
import com.central.parkapi.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<User> users = usersService.getUsers();
        List<UserResponseDTO> userResponseDTOList = UserMapper.INSTANCE.userListToUserResponseDTOList(users);
        return ResponseEntity.ok(userResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = usersService.getUserById(id);
        UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        User createdUser = usersService.createUser(user);
        UserResponseDTO createdUserDTO = UserMapper.INSTANCE.userToUserResponseDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @PatchMapping("/update-password/{id}")
    public ResponseEntity<?> updatePassword(@Valid @PathVariable Long id, @RequestBody UserAlterPasswordDTO userAlterPasswordDTO) {
        try {
            String result = usersService.updatePassword(id, userAlterPasswordDTO);
            return ResponseEntity.ok().body(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
