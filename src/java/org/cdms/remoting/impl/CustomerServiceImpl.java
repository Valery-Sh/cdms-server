/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.remoting.impl;

import java.util.ArrayList;
import org.cdms.domain.dao.EntityDao;
import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.entities.Customer;
import org.cdms.entities.Permission;
import org.cdms.remoting.CustomerService;
import org.cdms.remoting.QueryPage;
import org.cdms.remoting.validation.ValidationHandler;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

/**
 *
 * @author Valery
 */
public class CustomerServiceImpl<E extends Customer>  implements CustomerService<E> {

    private EntityDao<E> customerDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public CustomerServiceImpl() {
    }

    public void setCustomerDao(EntityDao<E> dao) {
        this.customerDao = dao;
    }

    public void setValidationHandler(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    public void setExceptionHandler(RemoteExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
   

    public E findById(long id) {
        E customer;
        try {
            customer = (E)customerDao.findById(id);
        } catch(Exception e) {
            customer = null;
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return customer;
    }

    @Override
    public E insert(E customer) {
        validationHandler.validate(customer);
        E result = null;
        try {
            result = (E)customerDao.insert(customer);
            if ( result != null ) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            }
            
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return result;
    }

    @Override
    public E update(E customer) {
        validationHandler.validate(customer);
        E result = null;
        //Authentication a = SecurityContextHolder.getContext().getAuthentication();
        try {
            result = (E)customerDao.update(customer);
            if ( result != null ) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            }

        } catch (Exception e) {
            
           if ( e instanceof HibernateOptimisticLockingFailureException) {
             HibernateOptimisticLockingFailureException ee = (HibernateOptimisticLockingFailureException)e;
             System.out.println("SERVER ERROR PUT " + e.getMessage() + "; class=" + e.getClass()); 
             System.out.println("---- className" + ee.getPersistentClassName()); 
             System.out.println("---- identifier" + ee.getIdentifier());              
           }
           exceptionHandler.throwDataAccessTranslated(e);
        }
        return result;
    }
    @Override
    public E delete(E c) {
        return deleteById(c.getId());
    }    
    @Override
    public E deleteById(Long id) {
        E result = null;
        try {
            result = (E)customerDao.delete(id); 
            if ( result != null ) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            } 
            
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        if ( result == null ) {
            exceptionHandler.throwDeleteFailure(id, Customer.class.getName());
        }
        return (E)result;
    }    

    @Override
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        QueryPage result = null;
        QueryPage r = queryPage;
        try {
            //result = customerDao.findByExample(queryPage);
            result = customerDao.findByExample(r);
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return result;

    }


    
}
