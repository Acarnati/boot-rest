package jm.dao;

import jm.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDAO {
    void createRole(Role role);
    Set<Role> getAllRole();
    Role getRoleById(Integer id);
    void updateRole(Role role);
    Role getRoleByRolename(String role);
}
