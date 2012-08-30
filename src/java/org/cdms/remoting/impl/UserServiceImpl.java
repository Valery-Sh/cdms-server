package org.cdms.remoting.impl;

import java.util.Locale;
import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.domain.dao.UserDao;
import org.cdms.shared.entities.User;
import org.cdms.shared.remoting.UserService;
import org.cdms.shared.remoting.validation.ValidationHandler;

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
            user = null;
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return user;
    }

    @Override
    public void insert(User user) {
        validationHandler.validate(user);
        try {
            userDao.insert(user);
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
    }

    @Override
    public void update(User user) {
        validationHandler.validate(user);
        try {
            userDao.update(user);
        } catch (Exception e) {
           exceptionHandler.throwDataAccessTranslated(e);
        }
    }
    @Override
    public void delete(long id) {
        try {
            userDao.delete(id); 
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
    }

    @Override
    public User findByUsername(String userName) {
        User user;
        try {
            Locale.setDefault(Locale.US);   
            user = userDao.findByUsername(userName);
        } catch(Exception e) {
            user = null;
            // Dont'throw exception since the method is used internally
        }
        return user;
        
    }
}
