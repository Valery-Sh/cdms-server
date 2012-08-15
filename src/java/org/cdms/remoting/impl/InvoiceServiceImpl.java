/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.remoting.impl;

import java.util.ArrayList;
import org.cdms.RemoteExceptionHandler;
import org.cdms.domain.dao.InvoiceDao;
import org.cdms.entities.Invoice;
import org.cdms.entities.Permission;
import org.cdms.remoting.InvoiceService;
import org.cdms.remoting.QueryPage;
import org.cdms.remoting.validation.ValidationHandler;

/**
 *
 * @author Valery
 */
public class InvoiceServiceImpl<E extends Invoice>  implements InvoiceService<E> {

    private InvoiceDao entityDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public InvoiceServiceImpl() {
    }

    public void setInvoiceDao(InvoiceDao dao) {
        this.entityDao = dao;
    }

    public void setValidationHandler(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    public void setExceptionHandler(RemoteExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
   

    @Override
    public E findById(long id) {
        E entity;
        try {
            entity = (E)entityDao.findById(id);
        } catch(Exception e) {
            entity = null;
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return entity;
    }

    @Override
    public E insert(E entity) {
        validationHandler.validate(entity);
        E result = null;
        try {
            result = (E)entityDao.insert(entity);
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
    public E update(E entity) {
        validationHandler.validate(entity);
        E result = null;

        try {
            result = (E)entityDao.update(entity);
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
        Invoice result = null;
        try {
            result = entityDao.delete(id); 
            if ( result != null ) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            } 
            
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        if ( result == null ) {
            exceptionHandler.throwDeleteFailure(id, Invoice.class.getName());
        }
        return (E)result;
    }    

    @Override
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        QueryPage result = null;
        QueryPage r = queryPage;
        try {
            result = entityDao.findByExample(r);
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        
        return result;

    }

}
