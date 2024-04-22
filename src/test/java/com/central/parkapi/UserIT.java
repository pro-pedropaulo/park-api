package com.central.parkapi;

import com.central.parkapi.Dtos.UserAlterPasswordDTO;
import com.central.parkapi.Dtos.UserResponseDTO;
import com.central.parkapi.Mappers.UserMapper;
import com.central.parkapi.entity.User;
import com.central.parkapi.exceptions.UserNotFoundException;
import com.central.parkapi.repository.UserRepository;
import com.central.parkapi.services.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUserTest_ValidData() throws Exception {
        User mockUser = new User();
        mockUser.setId_user(1L);
        mockUser.setUsername("testeemail@test.com");
        mockUser.setRole(User.Role.ROLE_ADMIN);

        UserResponseDTO userResponseDTO = UserMapper.INSTANCE.userToUserResponseDTO(mockUser);

        when(usersService.createUser(any(User.class))).thenReturn(mockUser);

        String userJson = "{\"username\":\"testeemail@test.com\",\"password\":\"222222\",\"role\":\"ROLE_ADMIN\"}";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testeemail@test.com"));
    }

    @Test
    public void createUserTest_InvalidEmail() throws Exception {
        // No need for mocking as this test should fail at the validation level
        String userJson = "{\"username\":\"notanemail\",\"password\":\"222222\",\"role\":\"ROLE_ADMIN\"}";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserTest_PasswordTooShort() throws Exception {
        // No need for mocking as this test should fail at the validation level
        String userJson = "{\"username\":\"testeemail@test.com\",\"password\":\"12345\",\"role\":\"ROLE_ADMIN\"}";

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUsersTest_Success() throws Exception {
        User user = new User();
        user.setId_user(2L);
        user.setUsername("exampleuser@test.com");

        List<User> userList = Arrays.asList(user);
        List<UserResponseDTO> userResponseDTOList = UserMapper.INSTANCE.userListToUserResponseDTOList(userList);

        when(usersService.getUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("exampleuser@test.com"));
    }

    @Test
    public void getUserByIdTest_Success() throws Exception {
        User user = new User();
        user.setId_user(1L);
        user.setUsername("sampleuser@test.com");

        // Mocking to return a User object directly
        when(usersService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_user").value(1))
                .andExpect(jsonPath("$.username").value("sampleuser@test.com"));
    }

    @Test
    public void getUserByIdTest_NotFound() throws Exception {
        when(usersService.getUserById(999L)).thenThrow(new UserNotFoundException("User not found with ID: 999"));

        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePassword_Success() throws Exception {
        Long userId = 1L;
        UserAlterPasswordDTO dto = new UserAlterPasswordDTO("123456", "654321", "654321");

        when(usersService.updatePassword(eq(userId), any(UserAlterPasswordDTO.class))).thenReturn("Password updated successfully");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/update-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actualPassword\":\"123456\",\"newPassword\":\"654321\",\"confirmPassword\":\"654321\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }


    @Test
    public void updatePassword_UserNotFound() throws Exception {
        Long userId = 999L;
        UserAlterPasswordDTO dto = new UserAlterPasswordDTO("123456", "654321", "654321");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());  // Simulating user not found
        when(usersService.updatePassword(eq(userId), any(UserAlterPasswordDTO.class)))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/update-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actualPassword\":\"123456\",\"newPassword\":\"654321\",\"confirmPassword\":\"654321\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void updatePassword_InvalidActualPassword() throws Exception {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId_user(userId);
        mockUser.setPassword("123456");

        UserAlterPasswordDTO dto = new UserAlterPasswordDTO("000000", "654321", "654321");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(usersService.updatePassword(eq(userId), any(UserAlterPasswordDTO.class)))
                .thenThrow(new RuntimeException("Invalid password"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/update-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actualPassword\":\"000000\",\"newPassword\":\"654321\",\"confirmPassword\":\"654321\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid password"));
    }

    @Test
    public void updatePassword_PasswordsDoNotMatch() throws Exception {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId_user(userId);
        mockUser.setPassword("123456");

        UserAlterPasswordDTO dto = new UserAlterPasswordDTO("123456", "654321", "123123");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(usersService.updatePassword(eq(userId), any(UserAlterPasswordDTO.class)))
                .thenThrow(new RuntimeException("Passwords do not match"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/update-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actualPassword\":\"123456\",\"newPassword\":\"654321\",\"confirmPassword\":\"123123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Passwords do not match"));
    }
    @Test
    public void updatePassword_NewPasswordSameAsOld() throws Exception {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId_user(userId);
        mockUser.setPassword("123456");

        UserAlterPasswordDTO dto = new UserAlterPasswordDTO("123456", "123456", "123456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(usersService.updatePassword(eq(userId), any(UserAlterPasswordDTO.class)))
                .thenThrow(new RuntimeException("New password must be different from the current password"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/update-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actualPassword\":\"123456\",\"newPassword\":\"123456\",\"confirmPassword\":\"123456\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("New password must be different from the current password"));
    }


}
