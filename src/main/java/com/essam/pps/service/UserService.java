package com.essam.pps.service;

import com.essam.pps.entity.AppUser;

public interface UserService {

    AppUser save(AppUser user);

    AppUser findById(Integer id);

    AppUser findByEmail(String email);
}
