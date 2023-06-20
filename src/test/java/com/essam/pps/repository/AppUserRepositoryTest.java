package com.essam.pps.repository;

import com.essam.pps.entity.AppUser;
import com.essam.pps.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository userRepository;

    @Test
    void CheckIfFindByEmailDoesNotReturnNull() {

        AppUser appUser = AppUser
                .builder()
                .email("a@a.com")
                .password("123")
                .roles(Collections.singletonList(Role.builder().name("ADMIN").build()))
                .build();

        userRepository.save(appUser);

        assertNotNull(userRepository.findByEmail("a@a.com"));
        assertEquals("a@a.com", userRepository.findByEmail("a@a.com").get().getEmail());
    }

    @Test
    void CheckIfFindByEmailReturnNull() {

        AppUser appUser = AppUser
                .builder()
                .email("a@a.com")
                .password("123")
                .roles(Collections.singletonList(Role.builder().name("ADMIN").build()))
                .build();

        userRepository.save(appUser);

        assertEquals(userRepository.findByEmail("a@aaa.com"), Optional.empty());
    }
}