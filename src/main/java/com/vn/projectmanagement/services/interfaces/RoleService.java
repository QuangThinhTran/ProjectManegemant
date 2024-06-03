package com.vn.projectmanagement.services.interfaces;

import com.vn.projectmanagement.models.Role;

import java.util.UUID;

public interface RoleService {
    Role findByName(String name);

    Role findById(UUID id);

    void createRole(String name);

    void updateRole(String name, String newName);

    void deleteByName(String name);

    void checkRoleExist(String name);
}
