/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.remoting.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cdms.RemoteExceptionHandler;
import org.cdms.domain.dao.InvoiceDao;
import org.cdms.entities.Invoice;
import org.cdms.entities.InvoiceItem;
import org.cdms.entities.Permission;
import org.cdms.entities.ProductItem;
import org.cdms.entities.User;
import org.cdms.remoting.InvoiceService;
import org.cdms.remoting.QueryPage;
import org.cdms.remoting.validation.ValidationHandler;

/**
 *
 * @author Valery
 */
public class InvoiceServiceImpl implements InvoiceService {

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
    public Invoice findById(long id) {
        Invoice entity;
        try {
            entity = entityDao.findById(id);
        } catch (Exception e) {
            entity = null;
            exceptionHandler.throwDataAccessTranslated(e);
        }
        return entity;
    }

    @Override
    public Invoice insert(Invoice entity) {
        validationHandler.validate(entity);
        Invoice result = null;
        try {
            result = entityDao.insert(entity);
            if (result != null) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            }

        } catch (Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);
        }
        return result;
    }

    @Override
    public Invoice update(Invoice entity) {
        validationHandler.validate(entity);
        Invoice result = null;
        //Authentication a = SecurityContextHolder.getContext().getAuthentication();
        try {
            result = entityDao.update(entity);
            if (result != null) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            }

        } catch (Exception e) {

            exceptionHandler.throwDataAccessTranslated(e);
        }
        return result;
    }

    @Override
    public Invoice delete(long id) {

        Invoice result = null;
        try {
            result = entityDao.delete(id);
            if (result != null) {
                result.getCreatedBy().setPassword(null); // not null !!!
                result.getCreatedBy().setPermissions(new ArrayList<Permission>());
            }

        } catch (Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);
        }
        if (result == null) {
            exceptionHandler.throwDeleteFailure(id, Invoice.class.getName());
        }
        return result;
    }

    @Override
    public QueryPage<Invoice> findByExample(QueryPage<Invoice> queryPage) {
        QueryPage<Invoice> result = null;
        try {
            result = entityDao.findByExample(queryPage);
        } catch (Exception e) {
            exceptionHandler.throwDataAccessTranslated(e);
        }
        for (Invoice entity : result.getQueryResult()) {

//            entity.getCustomer().setCreatedBy(null);
//            ArrayList a = new ArrayList();
//            a.addAll(entity.getCustomer().getCreatedBy().getPermissions());
            entity.getCustomer().getCreatedBy().setPermissions(new ArrayList());
            entity.getCreatedBy().setPermissions(new ArrayList());
            
//            entity.getCustomer().setCreatedBy(null);
            
            ArrayList<InvoiceItem> l = new ArrayList<InvoiceItem>();
            List<InvoiceItem> ilist = entity.getInvoiceItems();
            if (ilist != null) {
                l.addAll(ilist);
            }
            for ( InvoiceItem it : ilist ) {
                it.getProductItem().setStringPrice(it.getProductItem().getPrice().toPlainString());
            }
            entity.setInvoiceItems(l);
        }

        return result;

    }
}
