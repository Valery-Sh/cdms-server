/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cdms.entities.Customer;
import org.cdms.entities.Invoice;
import org.cdms.entities.InvoiceItem;
import org.cdms.entities.ProductItem;
import org.cdms.entities.User;
import org.cdms.remoting.QueryPage;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Valery
 */
public class InvoiceItemDaoImpl extends HibernateDaoSupport implements InvoiceItemDao{
    
    @Override
    @Transactional
    public InvoiceItem insert(InvoiceItem entity) {
        Invoice invoice = getHibernateTemplate().get(Invoice.class,entity.getInvoice().getId());        
        ProductItem productItem = getHibernateTemplate().get(ProductItem.class,entity.getProductItem().getId());        
        entity.setInvoice(invoice);
        entity.setProductItem(productItem);
        getHibernateTemplate().save(entity);
        //getHibernateTemplate().initialize(entity.getProductItem());
        return entity;
    }
    
    @Override
    @Transactional
    public InvoiceItem update(InvoiceItem entity) {
        getHibernateTemplate().get(InvoiceItem.class,entity.getId());
        InvoiceItem c = getHibernateTemplate().merge(entity);
        return c;
    }


    @Override
    @Transactional
    public InvoiceItem delete(Long id) {
        InvoiceItem result = new InvoiceItem();
        InvoiceItem c = getHibernateTemplate().get(InvoiceItem.class, id);
        if ( c != null ) {
            getHibernateTemplate().delete(c);
        }
        
        return c;
    }

    @Override
    @Transactional(readOnly=true)
    public InvoiceItem findById(Long id) {
        InvoiceItem entity = (InvoiceItem) getHibernateTemplate().get(InvoiceItem.class, id);
        return entity;
    }
    

    protected DetachedCriteria buildCriteriaByExample(QueryPage<InvoiceItem> queryPage) {
        InvoiceItem sample = queryPage.getEntityAsExample();
        Criterion c = CdmsCriteriaExample.createEx(queryPage.getEntityAsExample())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("version");
        DetachedCriteria invoiceItemCr = DetachedCriteria.forClass(ProductItem.class);
        invoiceItemCr.add(c);
        if ( sample.getIdFilter() != null) {
            invoiceItemCr.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        return invoiceItemCr;
    }
    @Override
    @Transactional(readOnly=true)    
    public QueryPage<InvoiceItem> findByExample(QueryPage<InvoiceItem> queryPage) {
        DetachedCriteria invoiceItemCr = buildCriteriaByExample(queryPage);
        InvoiceItem sample = queryPage.getEntityAsExample();
        
        invoiceItemCr.setProjection(Projections.rowCount());
        List rowCountList = getHibernateTemplate().findByCriteria(invoiceItemCr);
        
        int rowCount = ((Number)rowCountList.get(0)).intValue();
        queryPage.setRowCount(rowCount);
        
        invoiceItemCr = buildCriteriaByExample(queryPage);
        invoiceItemCr.addOrder(Order.asc("id"));
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();

        List<InvoiceItem> items = (List<InvoiceItem>) getHibernateTemplate().findByCriteria(invoiceItemCr,firstRec,queryPage.getPageSize());        
        queryPage.setQueryResult(items);
        queryPage.setEntityAsExample(null);

        return queryPage;

    }
    
}
