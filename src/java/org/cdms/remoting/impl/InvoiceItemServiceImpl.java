package org.cdms.remoting.impl;

import java.math.BigDecimal;
import org.cdms.domain.dao.EntityDao;
import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.shared.entities.Invoice;
import org.cdms.shared.entities.InvoiceItem;
import org.cdms.shared.entities.ProductItem;
import org.cdms.shared.remoting.InvoiceItemService;
import org.cdms.shared.remoting.QueryPage;
import org.cdms.shared.remoting.exception.RemoteValidationException;
import org.cdms.shared.remoting.validation.ValidationHandler;

/**
 *
 * @author V. Shyshkin
 */
public class InvoiceItemServiceImpl<E extends InvoiceItem> implements InvoiceItemService<E> {

    private EntityDao invoiceItemDao;
    private ValidationHandler validationHandler;
    private RemoteExceptionHandler exceptionHandler;

    public InvoiceItemServiceImpl() {
    }

    public void setInvoiceItemDao(EntityDao dao) {
        this.invoiceItemDao = dao;
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
            entity = (E) invoiceItemDao.findById(id);
            entity.getProductItem().setStringPrice(entity.getProductItem().getPrice().toPlainString());
        } catch (Exception e) {
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
            entity.setId(null);
            result = (E) invoiceItemDao.insert(entity);
        } catch (Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);
        }
        if ( result != null ) {
            result.getProductItem().getCreatedBy().setPermissions(null);
            result.setInvoice(new Invoice(result.getInvoice().getId()));
            result.getProductItem().setStringPrice(result.getProductItem().getPrice().toPlainString());
        }
        
        return result;
    }
    /**
     * Updates the the record in the database that is mapped to a 
     * specified entity.
     * The method completes the operation by calling the method 
     * {@link org.cdms.domain.dao.InvoiceItemDaoImpl#update(org.cdms.entities.InvoiceItem) } .
     * 
     * @param entity
     * @return a modified object of type <code>InvoiceItem</code>.
     */
    @Override
    public E update(E entity) {
        validationHandler.validate(entity);
        // Fix BigDecimal field since hessian for doesn't treat this type correctly. 
        entity.getProductItem().setPrice(new BigDecimal(entity.getProductItem().getStringPrice()));

        E result = null;
        try {
            result = (E) invoiceItemDao.update(entity);
            // Fix result due to Hessian cannot transfer Spring specific collections. 
            if (result != null) {
                Invoice inv = new Invoice(result.getInvoice().getId());
                result.setInvoice(inv);
                ProductItem pri = new ProductItem(result.getProductItem().getId());
                result.setProductItem(pri);
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
        InvoiceItem result = null;
        try {
            result = (E)invoiceItemDao.delete(id);
        } catch (Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);
        }
        if (result == null) {
            exceptionHandler.throwDeleteFailure(id, ProductItem.class.getName());
        }
        result.setInvoice(new Invoice(result.getInvoice().getId()));
        result.setProductItem(new ProductItem(result.getProductItem().getId()));
        
        return (E) result;
    }

    @Override
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        QueryPage result = null;
        QueryPage r = queryPage;
        try {
            result = invoiceItemDao.findByExample(r);
        } catch (Exception e) {
            if ( e instanceof RemoteValidationException ) {
                throw (RemoteValidationException)e;
            }

            exceptionHandler.throwDataAccessTranslated(e);
        }
        for (InvoiceItem it : queryPage.getQueryResult()) {
            it.getProductItem().setStringPrice(it.getProductItem().getPrice().toPlainString());
        }

        return result;

    }
}
