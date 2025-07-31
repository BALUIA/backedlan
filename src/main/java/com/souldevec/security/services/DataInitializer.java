package com.souldevec.security.services;

import com.souldevec.security.entities.Role;
import com.souldevec.security.entities.User;
import com.souldevec.security.enums.RoleList;
import com.souldevec.security.repositories.RoleRepository;
import com.souldevec.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario admin si no existe
        if (userRepository.findByUserName("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName(RoleList.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: El rol ROLE_ADMIN no se encuentra en la base de datos."));

            User adminUser = new User(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    adminRole
            );
            userRepository.save(adminUser);
            System.out.println("Usuario 'admin' creado exitosamente.");
        }
    }
}
