package com.policook.policook.serviceImpl;

import com.policook.policook.entity.User;
import com.policook.policook.payload.ApiResponse;
import com.policook.policook.payload.UserDto;
import com.policook.policook.repository.UserRepository;
import com.policook.policook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Override
    public ApiResponse getById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return new ApiResponse("USer not found",false,"User not found");

        return new ApiResponse("User id="+userId,true,userRepository.getById(userId));
    }

    @Override
    public ApiResponse addUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return new ApiResponse("Email is exist",false,"Email is exist");
        }
        User user=new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setSurName(userDto.getSurName());
        user.setName(userDto.getName());
        user.setRoles(Collections.singleton(userDto.getRole()));
        user.setBirthDate(userDto.getBirthDate());
        userRepository.save(user);
        return new ApiResponse("User added",true,"User added");
    }

}
