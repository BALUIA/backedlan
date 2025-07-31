package com.souldevec.security.services;

import com.souldevec.security.entities.Role;
import com.souldevec.security.enums.RoleList;
import com.souldevec.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.findByName(RoleList.ROLE_USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(RoleList.ROLE_USER);
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName(RoleList.ROLE_ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(RoleList.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }
    }
}
