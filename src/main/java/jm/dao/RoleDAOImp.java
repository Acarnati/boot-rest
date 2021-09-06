package jm.dao;

import org.springframework.stereotype.Repository;
import jm.model.Role;
import jm.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDAOImp implements RoleDAO {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void createRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Set<Role> getAllRole() {
        return new HashSet<>(entityManager.createQuery("from Role").getResultList());
    }

    @Override
    public Role getRoleById(Integer id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public void updateRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public Role getRoleByRolename(String role) {
        return getEntityManager()
                .createQuery("select u from Role u where u.role = :role", Role.class)
                .setParameter("role", role)
                .getSingleResult();
    }
}
