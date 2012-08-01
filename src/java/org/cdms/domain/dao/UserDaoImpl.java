package org.cdms.domain.dao;

import java.util.ArrayList;
import java.util.List;
import org.cdms.entities.Permission;
import org.cdms.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
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
        //getHibernateTemplate().getSessionFactory().getCurrentSession().
        if ( user != null && user.getPermissions() != null && ! user.getPermissions().isEmpty() ) {
             List l = new ArrayList();  
             l.addAll(user.getPermissions());
             user.setPermissions(l);
            //Permission p = user.getPermissions().get(0);
        }
        return user;
    }
    
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
        //PageListHolder p;
        //getHibernateTemplate().f
        //Criteria c = new DetachedCriteria();
        //Session s;
        //DetachedCriteria d;
        return null;
        
    }
}
