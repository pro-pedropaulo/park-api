package com.central.parkapi.Controller;

import com.central.parkapi.Dtos.UserAlterPasswordDTO;
import com.central.parkapi.Dtos.UserDTO;
import com.central.parkapi.Dtos.UserResponseDTO;
import com.central.parkapi.Mappers.UserMapper;
import com.central.parkapi.entity.User;
import com.central.parkapi.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Contains all the endpoints related to users")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UsersService usersService;

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<User> users = usersService.getUsers();
        List<UserResponseDTO> userResponseDTOList = UserMapper.INSTANCE.userListToUserResponseDTOList(users);
        return ResponseEntity.ok(userResponseDTOList);
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = usersService.getUserById(id);
        UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(user);
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        User createdUser = usersService.createUser(user);
        UserResponseDTO createdUserDTO = UserMapper.INSTANCE.userToUserResponseDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @Operation(summary = "Update user password")
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
