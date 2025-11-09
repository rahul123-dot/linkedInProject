package com.codingshuttle.linkedInProject.userService.service;

import com.codingshuttle.linkedInProject.userService.dto.LoginRequestDto;
import com.codingshuttle.linkedInProject.userService.dto.SignupRequestDto;
import com.codingshuttle.linkedInProject.userService.dto.UserDto;
import com.codingshuttle.linkedInProject.userService.entity.User;
import com.codingshuttle.linkedInProject.userService.event.UserCreated;
import com.codingshuttle.linkedInProject.userService.exception.BadRequestException;
import com.codingshuttle.linkedInProject.userService.exception.ResourceNotFoundException;
import com.codingshuttle.linkedInProject.userService.repository.UserRepository;
import com.codingshuttle.linkedInProject.userService.utils.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final KafkaTemplate<Long,UserCreated> userCreatedKafkaTopic;

    public UserDto signUp(SignupRequestDto signupRequestDto) {
        log.info("Signup a user with email: {}", signupRequestDto.getEmail());

        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists) {
            throw new BadRequestException("User already exists");
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(BCrypt.hash(signupRequestDto.getPassword()));

        user = userRepository.save(user);

        UserCreated userCreated = UserCreated.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();
        userCreatedKafkaTopic.send("User_Created_Topic",userCreated);

        return modelMapper.map(user, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
        log.info("Login request for user with email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new BadRequestException(
                "Incorrect email or password"));

        boolean isPasswordMatch = BCrypt.match(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) {
            throw new BadRequestException("Incorrect email or password");
        }

        return jwtService.generateAccessToken(user);
    }
}
