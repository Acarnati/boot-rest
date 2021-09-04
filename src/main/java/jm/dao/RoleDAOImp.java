package jm.dao;

import org.springframework.stereotype.Repository;
import jm.model.Role;
import jm.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
    public List<Role> getAllRole() {
        return entityManager.createQuery("from Role").getResultList();
    }

    @Override
    public Role getRoleById(int id) {
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
