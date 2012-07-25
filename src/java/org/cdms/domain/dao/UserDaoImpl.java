package org.cdms.domain.dao;

import java.util.List;
import org.cdms.domain.User;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author V. Shyshkin
 */
public class UserDaoImpl  extends HibernateDaoSupport implements UserDao {

    public UserDaoImpl() {
    }
    
    @Override
    @Transactional
    public void insert(User user) {
        getHibernateTemplate().save(user);
    }
    
    @Override
    @Transactional
    public void update(User user) {
        getHibernateTemplate().get(User.class,user.getId());
        getHibernateTemplate().merge(user);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        User user = (User)getHibernateTemplate().get(User.class,id);
        getHibernateTemplate().delete(id);
    }

    @Override
    @Transactional(readOnly=true)
    public User findById(Long id) {
        return (User) getHibernateTemplate().get(User.class, id);
    }

    @Override
    @Transactional(readOnly=true)
    public List<User> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
