package com.poc.parkapi.web.dto.mapper;

import com.poc.parkapi.entity.User;
import com.poc.parkapi.web.dto.user.CreateUserDto;
import com.poc.parkapi.web.dto.user.UserResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(CreateUserDto createUserDto) {
        return new ModelMapper().map(createUserDto, User.class);
    }

    public static UserResponseDto toResponseDto(User user) {
        String role = user.getRole().name().substring("ROLE_".length());

        PropertyMap<User, UserResponseDto> props = new PropertyMap<User, UserResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return mapper.map(user, UserResponseDto.class);
    }

    public static List<UserResponseDto> toResponseListDto(List<User> users) {
        return users.stream().map(UserMapper::toResponseDto).collect(Collectors.toList());
    }

}
