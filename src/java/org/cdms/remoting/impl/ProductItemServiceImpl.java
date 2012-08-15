/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.remoting.impl;

import java.util.ArrayList;
import java.util.List;
import org.cdms.RemoteExceptionHandler;
import org.cdms.domain.dao.ProductItemDao;
import org.cdms.entities.InvoiceItem;
import org.cdms.entities.Permission;
import org.cdms.entities.ProductItem;
import org.cdms.remoting.ProductItemService;
import org.cdms.remoting.QueryPage;
import org.cdms.remoting.validation.ValidationHandler;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

/**
 *
 * @author Valery
 */
public class ProductItemServiceImpl<E extends ProductItem>  implements ProductItemService<E> {

    private ProductItemDao productItemDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public ProductItemServiceImpl() {
    }

    public void setProductItemDao(ProductItemDao dao) {
        this.productItemDao = dao;
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
            entity = (E)productItemDao.findById(id);
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
            result = (E)productItemDao.insert(entity);
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
        //Authentication a = SecurityContextHolder.getContext().getAuthentication();
        try {
            result = (E)productItemDao.update(entity);
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
        ProductItem result = null;
        try {
            result = productItemDao.delete(id); 
            if ( result != null ) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            } 
            
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
            result = productItemDao.findByExample(r);
        } catch(Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        for ( ProductItem it :queryPage.getQueryResult() ) {
            it.setStringPrice(it.getPrice().toPlainString());
        }
        
        return result;

    }


    
}
