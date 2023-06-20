package com.essam.pps.repository;

import com.essam.pps.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void checkIfFindRoleByNameReturnsTheCorrectValue() {

        Role role = Role.builder()
                .name("STUDENT")
                .build();
        roleRepository.save(role);

        assertEquals(role, roleRepository.findRoleByName("STUDENT").get());
    }
}