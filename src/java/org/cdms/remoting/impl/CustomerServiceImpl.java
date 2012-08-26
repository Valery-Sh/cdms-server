package org.cdms.remoting.impl;

import java.util.ArrayList;
import org.cdms.domain.dao.EntityDao;
import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.shared.entities.Customer;
import org.cdms.shared.entities.Permission;
import org.cdms.shared.remoting.CustomerService;
import org.cdms.shared.remoting.QueryPage;
import org.cdms.shared.remoting.exception.RemoteValidationException;
import org.cdms.shared.remoting.validation.ValidationHandler;

/**
 *
 * @author V. Shyshkin
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
            if ( e instanceof RemoteValidationException ) {
                throw (RemoteValidationException)e;
            }
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return result;

    }


    
}
