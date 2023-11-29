package com.poc.parkapi.web.controller;

import com.poc.parkapi.entity.User;
import com.poc.parkapi.service.UserService;
import com.poc.parkapi.web.dto.CreateUserDto;
import com.poc.parkapi.web.dto.UpdateUserPasswordDto;
import com.poc.parkapi.web.dto.UserResponseDto;
import com.poc.parkapi.web.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody CreateUserDto createUserDto) {
        User response = userService.save(UserMapper.toUser(createUserDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDto(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId) {
        User response = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toResponseDto(response));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> response = userService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toResponseListDto(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable("id") Long userId, @RequestBody UpdateUserPasswordDto updateUserPasswordDto) {
        userService.updatePassword(userId, updateUserPasswordDto.getPassword(), updateUserPasswordDto.getNewPassword(), updateUserPasswordDto.getConfirmNewPassword());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
