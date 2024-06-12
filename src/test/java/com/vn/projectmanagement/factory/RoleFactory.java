package com.vn.projectmanagement.factory;

import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.repositories.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

    private final RoleRepository roleRepository;

    public RoleFactory(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role create(int index) {
        Role role = new Role();
        role.setName("Role " + index);
        return roleRepository.save(role);
    }

    public void delete() {
        roleRepository.deleteAll();
    }
}
