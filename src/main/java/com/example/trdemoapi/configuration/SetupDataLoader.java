package com.example.trdemoapi.configuration;

import com.example.trdemoapi.model.Role;
import com.example.trdemoapi.model.User;
import com.example.trdemoapi.repository.RoleRepository;
import com.example.trdemoapi.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        var adminRole = createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_STUDENT");
        createRoleIfNotFound("ROLE_TEACHER");


        var user = userRepository.findByEmail("test@test.com");
        if (user.isEmpty()) {
            userRepository.save(new User()
                    .setName("Test")
                    .setEmail("test@test.com")
                    .setPassword(passwordEncoder.encode("test"))
                    .setRoles(Collections.singletonList(adminRole)));
        }

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role().setName(name);
            roleRepository.save(role);
        }
        return role;
    }
}
