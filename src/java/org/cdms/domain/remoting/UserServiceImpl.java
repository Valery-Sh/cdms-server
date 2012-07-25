/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.remoting;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.cdms.RemoteExceptionHandler;
import org.cdms.RemoteValidationException;
import org.cdms.ValidationHandler;
import org.cdms.domain.User;
import org.cdms.domain.dao.UserDao;
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
//     Validator v;
//     ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory(); 
//     v = validatorFactory.getValidator();  


        User u = new User();
        u.setLastName("V");
        //TODO remove two lines
        if ( id > 60 )
            validationHandler.validate(u);
        User user = userDao.findById(id);
        return user;
    }

    @Override
    public void insert(User user) {
        validationHandler.validate(user);
        userDao.insert(user);
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
        }
    }
    @Override
    public void delete(long id) {
        userDao.delete(id);
    }
}
