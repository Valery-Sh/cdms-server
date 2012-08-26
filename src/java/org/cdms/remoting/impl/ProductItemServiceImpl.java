package org.cdms.remoting.impl;

import java.util.ArrayList;
import org.cdms.domain.dao.EntityDao;
import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.shared.entities.Permission;
import org.cdms.shared.entities.ProductItem;
import org.cdms.shared.remoting.ProductItemService;
import org.cdms.shared.remoting.QueryPage;
import org.cdms.shared.remoting.exception.RemoteValidationException;
import org.cdms.shared.remoting.validation.ValidationHandler;

/**
 *
 * @author V. Shyshkin
 */
public class ProductItemServiceImpl<E extends ProductItem>  implements ProductItemService<E> {

    private EntityDao productItemDao;

    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;
    
    public ProductItemServiceImpl() {
    }

    public void setProductItemDao(EntityDao dao) {
        this.productItemDao = dao;
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
            result = (E)productItemDao.delete(id); 
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
        return result;
    }    

    @Override
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        QueryPage result = null;
        QueryPage r = queryPage;
        try {
            result = productItemDao.findByExample(r);
        } catch(Exception e) {
            if ( e instanceof RemoteValidationException ) {
                throw (RemoteValidationException)e;
            }
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        for ( E it :queryPage.getQueryResult() ) {
            it.setStringPrice(it.getPrice().toPlainString());
        }
        
        return result;

    }


    
}
