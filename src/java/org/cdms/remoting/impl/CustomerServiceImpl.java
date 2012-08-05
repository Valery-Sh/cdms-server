/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.remoting.impl;

import java.util.List;
import org.cdms.RemoteExceptionHandler;
import org.cdms.ValidationHandler;
import org.cdms.domain.dao.CustomerDao;
import org.cdms.entities.Customer;
import org.cdms.remoting.CustomerService;
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
//org.springframework.remoting.caucho.HessianServiceExporter e;

        //user = userDao.findById(id);
        return customer;
    }

    @Override
    public void insert(Customer customer) {
        validationHandler.validate(customer);
        try {
            customerDao.insert(customer);
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
    }

    @Override
    public void update(Customer customer) {
        validationHandler.validate(customer);
        //Authentication a = SecurityContextHolder.getContext().getAuthentication();
        try {
            customerDao.update(customer);
        } catch (Exception e) {
            
           if ( e instanceof HibernateOptimisticLockingFailureException) {
             HibernateOptimisticLockingFailureException ee = (HibernateOptimisticLockingFailureException)e;
             System.out.println("SERVER ERROR PUT " + e.getMessage() + "; class=" + e.getClass()); 
             System.out.println("---- className" + ee.getPersistentClassName()); 
             System.out.println("---- identifier" + ee.getIdentifier());              
           }
           exceptionHandler.throwDataAccessTranslated(e);
        }
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
