package jm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jm.dao.RoleDAO;
import jm.model.Role;

import java.util.Set;

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
    public Set<Role> getAllRole() {
        return roleDAO.getAllRole();
    }
}
