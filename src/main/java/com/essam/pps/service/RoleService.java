package com.essam.pps.service;

import com.essam.pps.entity.Role;

public interface RoleService {

    void save(Role role);

    Role findByName(String name);

    Role findById(Integer id);

}
