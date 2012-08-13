/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cdms.entities.Invoice;
import org.cdms.entities.InvoiceItem;
import org.cdms.entities.Permission;
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
public class InvoiceDaoImpl extends HibernateDaoSupport implements InvoiceDao{
    
    @Override
    @Transactional
    public Invoice insert(Invoice entity) {
        User u = getHibernateTemplate().get(User.class,entity.getCreatedBy().getId());
        entity.setCreatedBy(u);
        entity.setCreatedAt(new Date());

        getHibernateTemplate().save(entity);
        getHibernateTemplate().initialize(entity.getCreatedBy());
        return entity;
    }
    
    @Override
    @Transactional
    public Invoice update(Invoice entity) {
        getHibernateTemplate().get(Invoice.class,entity.getId());
        Invoice c = getHibernateTemplate().merge(entity);
        if ( c != null ) {
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        return c;
    }


    @Override
    @Transactional
    public Invoice delete(Long id) {
        Invoice result = new Invoice();
        Invoice c = getHibernateTemplate().get(Invoice.class, id);
        if ( c != null ) {
            User u = c.getCreatedBy();
            getHibernateTemplate().delete(c);
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        
        return c;
    }

    @Override
    @Transactional(readOnly=true)
    public Invoice findById(Long id) {
        Invoice entity = (Invoice) getHibernateTemplate().get(Invoice.class, id);
        return entity;
    }

    protected DetachedCriteria buildCriteriaByExample(QueryPage<Invoice> queryPage) {
        Invoice sample = queryPage.getEntityAsExample();
        Criterion c = CdmsCriteriaExample.createEx(queryPage.getEntityAsExample())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("customer") 
                .excludeProperty("invoiceItems")                                
                .excludeProperty("createdAt")
                .excludeProperty("version");
        
        DetachedCriteria mainCriteria = DetachedCriteria.forClass(Invoice.class);
        mainCriteria.add(c);
        if ( sample.getIdFilter() != null) {
            mainCriteria.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        if ( sample.getCreatedAt() != null) {
            if ( sample.getCreatedAtEnd() == null ) {
                sample.setCreatedAtEnd(sample.getCreatedAt());
            }
            mainCriteria.add(Restrictions.between("createdAt", sample.getCreatedAt(), sample.getCreatedAtEnd()));
        } else if ( sample.getCreatedAtEnd() != null) {
            mainCriteria.add(Restrictions.le("createdAt", sample.getCreatedAtEnd()));
        }

        DetachedCriteria userCr = mainCriteria.createCriteria("createdBy");
        Criterion u = CdmsCriteriaExample.createEx(sample.getCreatedBy())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("version")
                .excludeProperty("permissions");
        
        userCr.add(u);
        
        DetachedCriteria customerCr = mainCriteria.createCriteria("customer");
        Criterion cust = CdmsCriteriaExample.createEx(sample.getCustomer())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("createdBy")
                .excludeProperty("createdAt")
                .excludeProperty("version");
        
        customerCr.add(cust);

        
        return mainCriteria;
    }
    @Override
    @Transactional(readOnly=true)    
    public QueryPage<Invoice> findByExample(QueryPage<Invoice> queryPage) {
        DetachedCriteria mainCriteria = buildCriteriaByExample(queryPage);
        Invoice sample = queryPage.getEntityAsExample();
        
        mainCriteria.setProjection(Projections.rowCount());
        List rowCountList = getHibernateTemplate().findByCriteria(mainCriteria);
        
        int rowCount = ((Number)rowCountList.get(0)).intValue();
        queryPage.setRowCount(rowCount);
        
        mainCriteria = buildCriteriaByExample(queryPage);
        mainCriteria.addOrder(Order.asc("id"));
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();

        List<Invoice> entities = (List<Invoice>) getHibernateTemplate().findByCriteria(mainCriteria,firstRec,queryPage.getPageSize());        
        List<Invoice> list = new ArrayList<Invoice>();
        list.addAll(entities);
        for ( Invoice entity : list) {
            getHibernateTemplate().initialize(entity.getInvoiceItems());
            getHibernateTemplate().initialize(entity.getCustomer().getCreatedBy().getPermissions());            
            getHibernateTemplate().initialize(entity.getCreatedBy().getPermissions());                        
        }            
        queryPage.setQueryResult(list);
        queryPage.setEntityAsExample(null);
        return queryPage;

    }
    
}
