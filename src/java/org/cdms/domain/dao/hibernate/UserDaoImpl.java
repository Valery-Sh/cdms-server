package org.cdms.domain.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import org.cdms.domain.dao.UserDao;
import org.cdms.shared.entities.User;
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
        //User user = (User)getHibernateTemplate()..get(User.class,id);
        getHibernateTemplate().delete(id);
    }

    @Override
    @Transactional(readOnly=true)
    public User findById(Long id) {
        User user = (User) getHibernateTemplate().get(User.class, id);
        if ( user != null ) {
            getHibernateTemplate().initialize(user.getPermissions());
        }
        if ( user != null && user.getPermissions() != null && ! user.getPermissions().isEmpty() ) {
             List l = new ArrayList();  
             l.addAll(user.getPermissions());
             user.setPermissions(l);
        }
        return user;
    }
    /**
     *  The method is used when authenticate.
     * @param userName 
     * @return 
     */
    @Transactional(readOnly=true)
    @Override
    public User findByUsername(String userName) {
        List<User> users = (List<User>) getHibernateTemplate().
                find("from User where userName=?", userName);
        User user = null;
        
        if ( users != null && !users.isEmpty()) {
            user = users.get(0);
        }            
        if ( user != null ) {
            getHibernateTemplate().initialize(user.getPermissions());
        }
        if ( user != null && user.getPermissions() != null && ! user.getPermissions().isEmpty() ) {
             List l = new ArrayList();  
             l.addAll(user.getPermissions());
             user.setPermissions(l);
        }
        return user;
    }

    @Override
    @Transactional(readOnly=true)
    public List<User> findAll() {
        return null;
    }
}
