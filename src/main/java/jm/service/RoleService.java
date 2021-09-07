package jm.service;

import jm.model.Role;

import java.util.Set;

public interface RoleService {
    void createRole(Role role);
    Set<Role> getAllRole();
}
