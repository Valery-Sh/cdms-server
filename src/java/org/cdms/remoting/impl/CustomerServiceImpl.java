/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.remoting.impl;

import java.util.ArrayList;
import java.util.List;
import org.cdms.RemoteExceptionHandler;

import org.cdms.domain.dao.CustomerDao;
import org.cdms.entities.Customer;
import org.cdms.entities.Permission;
import org.cdms.remoting.CustomerService;
import org.cdms.remoting.validation.ValidationHandler;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

/**
 *
 * @author Valery
 */
public class CustomerServiceImpl  implements CustomerService {

    private CustomerDao customerDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public CustomerServiceImpl() {
    }

    public void setCustomerDao(CustomerDao dao) {
        this.customerDao = dao;
    }

    public void setValidationHandler(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    public void setExceptionHandler(RemoteExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
   

    @Override
    public Customer findById(long id) {
        Customer customer;
        try {
            customer = customerDao.findById(id);
        } catch(Exception e) {
            customer = null;
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return customer;
    }

    @Override
    public Customer insert(Customer customer) {
        validationHandler.validate(customer);
        Customer result = null;
        try {
            result = customerDao.insert(customer);
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
    public Customer update(Customer customer) {
        validationHandler.validate(customer);
        Customer result = null;
        //Authentication a = SecurityContextHolder.getContext().getAuthentication();
        try {
            result = customerDao.update(customer);
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
    public void delete(long id) {
        try {
            customerDao.delete(id); 
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
    }    

    @Override
    public List<Customer> findByExample(Customer sample, int start,int pageSize) {
        List<Customer> customers;
        try {
            customers = customerDao.findByExample(sample,start, pageSize);
        } catch(Exception e) {
            customers = null;
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return customers;

    }
}
