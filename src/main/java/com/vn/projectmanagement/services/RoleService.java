package com.vn.projectmanagement.services;

import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.repositories.RoleRepository;
import com.vn.projectmanagement.services.interfaces.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleService implements RoleServiceImpl {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Find role by name
     * @param name - name of role
     * @return Role
     */
    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    /**
     * Find role by id
     * @param id - id of role
     * @return Role
     */
    @Override
    public Role findById(UUID id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    /**
     * Create role by name
     * @param name - name of role
     */
    @Override
    public void createRole(String name)
    {
        Role role = new Role();
        role.setName(name);
        roleRepository.save(role);
    }

    /**
     * Update role by name
     * @param name    - name of role
     * @param newName - new name of role
     */
    @Override
    public void updateRole(String name, String newName) {
        Role roleCurrent = findByName(name);
        roleCurrent.setName(newName);
        roleRepository.save(roleCurrent);
    }

    /**
     * Delete role by name
     * @param name - name of role
     */
    @Override
    public void deleteByName(String name) {
        Role roleCurrent = findByName(name);
        roleRepository.deleteById(roleCurrent.getId());
    }
}
