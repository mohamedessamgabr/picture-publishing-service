package com.essam.pps;

import com.essam.pps.entity.AppUser;
import com.essam.pps.entity.Role;
import com.essam.pps.service.RoleService;
import com.essam.pps.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class PicturePublishingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicturePublishingServiceApplication.class, args);
	}



	@Bean
	public CommandLineRunner commandLineRunner(RoleService roleService,
											   UserService userService,
											   PasswordEncoder passwordEncoder) {
		return runner -> {
			saveRoles(roleService);
			saveAdmin(userService, passwordEncoder, roleService);
		};
	}

	public void saveRoles(RoleService roleService) {
		Role adminRole = roleService.findByName("ADMIN");
		Role userRole = roleService.findByName("USER");

		if(adminRole == null) {
			Role admin = Role
					.builder()
					.name("ADMIN")
					.build();
			roleService.save(admin);
		}
		if(userRole == null) {
			Role user = Role
					.builder()
					.name("USER")
					.build();
			roleService.save(user);
		}


	}


	public void saveAdmin(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
		AppUser adminUser = userService.findByEmail("admin");
		if(adminUser == null) {
			Role adminRole = roleService.findByName("ADMIN");
			AppUser admin = AppUser
					.builder()
					.email("admin")
					.password(passwordEncoder.encode("admin123"))
					.build();
			admin.setRoles(Collections.singletonList(adminRole));
			userService.save(admin);
		}
	}

}
