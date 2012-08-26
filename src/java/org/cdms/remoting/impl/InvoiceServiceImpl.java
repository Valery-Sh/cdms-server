
package org.cdms.remoting.impl;

import java.util.ArrayList;
import org.cdms.domain.dao.EntityDao;
import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.shared.entities.Invoice;
import org.cdms.shared.entities.InvoiceItem;
import org.cdms.shared.entities.Permission;
import org.cdms.shared.remoting.InvoiceService;
import org.cdms.shared.remoting.QueryPage;
import org.cdms.shared.remoting.exception.RemoteValidationException;
import org.cdms.shared.remoting.validation.ValidationHandler;

/**
 *
 * @author V. Shyshkin
 */
public class InvoiceServiceImpl<E extends Invoice>  implements InvoiceService<E> {

    private EntityDao entityDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public InvoiceServiceImpl() {
    }

    public void setInvoiceDao(EntityDao dao) {
        this.entityDao = dao;
    }

    public void setValidationHandler(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    public void setExceptionHandler(RemoteExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
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
            result = (E)entityDao.delete(id); 
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
            if ( e instanceof RemoteValidationException ) {
                throw (RemoteValidationException)e;
            }
            
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        for ( Invoice invoice : queryPage.getQueryResult() ) {
            for ( InvoiceItem invoiceItem : invoice.getInvoiceItems() ) {
                invoiceItem.getProductItem().setStringPrice(invoiceItem.getProductItem().getPrice().toPlainString());
            }
        }
        
        return result;

    }

}
