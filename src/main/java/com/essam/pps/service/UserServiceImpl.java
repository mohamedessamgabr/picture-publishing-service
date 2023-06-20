package com.essam.pps.service;

import com.essam.pps.entity.AppUser;
import com.essam.pps.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AppUserRepository userRepository;
    @Override
    public AppUser save(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public AppUser findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
