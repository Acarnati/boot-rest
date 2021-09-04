package jm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jm.dao.RoleDAO;
import jm.model.Role;

import java.util.List;

@Service
@Transactional
public class RoleServiceImp implements RoleService {
    private RoleDAO roleDAO;

    @Autowired
    public void setRoleRepo(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public void createRole(Role role) {
        roleDAO.createRole(role);
    }

    @Override
    public List<Role> getAllRole() {
        return roleDAO.getAllRole();
    }

    @Override
    public Role getRoleById(int id) {
        return roleDAO.getRoleById(id);
    }

    @Override
    public void updateRole(Role role) {
        roleDAO.updateRole(role);
    }

    @Override
    public Role getRoleByRolename(String role) {
        return roleDAO.getRoleByRolename(role);
    }
}
