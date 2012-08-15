/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.remoting.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.cdms.RemoteExceptionHandler;
import org.cdms.domain.dao.InvoiceItemDao;
import org.cdms.domain.dao.ProductItemDao;
import org.cdms.entities.InvoiceItem;
import org.cdms.entities.Permission;
import org.cdms.entities.ProductItem;
import org.cdms.remoting.InvoiceItemService;
import org.cdms.remoting.ProductItemService;
import org.cdms.remoting.QueryPage;
import org.cdms.remoting.validation.ValidationHandler;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

/**
 *
 * @author Valery
 */
public class InvoiceItemServiceImpl<E extends InvoiceItem>  implements InvoiceItemService<E> {

    private InvoiceItemDao invoiceItemDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public InvoiceItemServiceImpl() {
    }

    public void setInvoiceItemDao(InvoiceItemDao dao) {
        this.invoiceItemDao = dao;
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
            entity = (E)invoiceItemDao.findById(id);
            entity.getProductItem().setStringPrice(entity.getProductItem().getPrice().toPlainString());
        } catch(Exception e) {
            entity = null;
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return entity;
    }

    @Override
    public E insert(E entity) {
        validationHandler.validate(entity);
        entity.getProductItem().setPrice(new BigDecimal(entity.getProductItem().getStringPrice()));        
        
        E result = null;
        try {
            result = (E)invoiceItemDao.insert(entity);
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        return result;
    }

    @Override
    public E update(E entity) {
        validationHandler.validate(entity);
        entity.getProductItem().setPrice(new BigDecimal(entity.getProductItem().getStringPrice()));        
        
        E result = null;
        try {
            result = (E)invoiceItemDao.update(entity);
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
        InvoiceItem result = null;
        try {
            result = invoiceItemDao.delete(id); 
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        if ( result == null ) {
            exceptionHandler.throwDeleteFailure(id, ProductItem.class.getName());
        }
        return (E)result;
    }    

    @Override
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        QueryPage result = null;
        QueryPage r = queryPage;
        try {
            result = invoiceItemDao.findByExample(r);
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        for ( InvoiceItem it : queryPage.getQueryResult() ) {
            it.getProductItem().setStringPrice(it.getProductItem().getPrice().toPlainString());
        }
        
        return result;

    }


    
}
