package com.peixoto.api.services;


import com.peixoto.api.domain.Users;
import com.peixoto.api.dto.UserDto;
import com.peixoto.api.repository.UserRepository;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Generated
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public List<UserDto> listAllUsers() {
        List<Users> all = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        all.forEach(user -> {
            var userDto = new UserDto();
            userDto.setUsername(user.getUsername());
            userDto.setAuthhorities(user.getAuthhorities());
            usersDto.add(userDto);
        });
       return usersDto;
    }

}
