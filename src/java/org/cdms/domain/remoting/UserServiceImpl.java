/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.remoting;

import org.cdms.RemoteExceptionHandler;
import org.cdms.ValidationHandler;
import org.cdms.domain.dao.UserDao;
import org.cdms.entities.User;
import org.cdms.remoting.UserService;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

/**
 *
 * @author V. Shyshkin
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public UserServiceImpl() {
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setValidationHandler(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    public void setExceptionHandler(RemoteExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public User findById(long id) {
        User user;
        try {
            user = userDao.findById(id);
        } catch(Exception e) {
            exceptionHandler.throwTranslated(e);        
        }
//org.springframework.remoting.caucho.HessianServiceExporter e;

        user = userDao.findById(id);
        return user;
    }

    @Override
    public void insert(User user) {
        validationHandler.validate(user);
        try {
            userDao.insert(user);
        } catch(Exception e) {
            exceptionHandler.throwTranslated(e);        
        }
    }

    @Override
    public void update(User user) {
        validationHandler.validate(user);
        try {
            userDao.update(user);
        } catch (Exception e) {
            
           if ( e instanceof HibernateOptimisticLockingFailureException) {
             HibernateOptimisticLockingFailureException ee = (HibernateOptimisticLockingFailureException)e;
             System.out.println("SERVER ERROR PUT " + e.getMessage() + "; class=" + e.getClass()); 
             System.out.println("---- className" + ee.getPersistentClassName()); 
             System.out.println("---- identifier" + ee.getIdentifier());              
           }
           exceptionHandler.throwTranslated(e);
        }
    }
    @Override
    public void delete(long id) {
        try {
            userDao.delete(id); 
        } catch(Exception e) {
            exceptionHandler.throwTranslated(e);        
        }
    }

    @Override
    public User authenticate(String userName, String password) {
        return userDao.find(userName,password);
    }
}
