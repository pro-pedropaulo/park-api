package com.central.parkapi.Mappers;

import com.central.parkapi.Dtos.UserDTO;
import com.central.parkapi.Dtos.UserResponseDTO;
import com.central.parkapi.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Mapeamento de User para UserDTO
    @Mapping(source = "id_user", target = "id_user")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "created_at", target = "created_at")
    @Mapping(source = "updated_at", target = "updated_at")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "updatedBy", target = "updatedBy")
    UserDTO userToUserDTO(User user);

    // Mapeamento de UserDTO para User
    @Mapping(source = "id_user", target = "id_user")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "created_at", target = "created_at")
    @Mapping(source = "updated_at", target = "updated_at")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "updatedBy", target = "updatedBy")
    User userDTOToUser(UserDTO userDTO);

    @AfterMapping
    default void convertRole(User user, @MappingTarget UserResponseDTO userResponseDTO) {
        if (user.getRole() != null) {
            userResponseDTO.setRole(user.getRole().name().substring(5));
        }
    }
    UserResponseDTO userToUserResponseDTO(User user);
    List<UserResponseDTO> userListToUserResponseDTOList(List<User> users);
}
