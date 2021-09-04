package jm.dao;

import jm.model.Role;

import java.util.List;

public interface RoleDAO {
    void createRole(Role role);
    List<Role> getAllRole();
    Role getRoleById(int id);
    void updateRole(Role role);
    Role getRoleByRolename(String role);
}
