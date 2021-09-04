package jm.service;

import jm.model.Role;

import java.util.List;

public interface RoleService {
    void createRole(Role role);
    List<Role> getAllRole();
    Role getRoleById(int id);
    void updateRole(Role role);
    Role getRoleByRolename(String role);
}
