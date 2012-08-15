/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cdms.entities.Customer;
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
public class ProductItemDaoImpl extends HibernateDaoSupport implements ProductItemDao{
    
    @Override
    @Transactional
    public ProductItem insert(ProductItem entity) {
        User u = getHibernateTemplate().get(User.class,entity.getCreatedBy().getId());
        entity.setCreatedBy(u);
        entity.setCreatedAt(new Date());

        getHibernateTemplate().save(entity);
        getHibernateTemplate().initialize(entity.getCreatedBy());
        return entity;
    }
    
    @Override
    @Transactional
    public ProductItem update(ProductItem entity) {
        getHibernateTemplate().get(ProductItem.class,entity.getId());
        ProductItem c = getHibernateTemplate().merge(entity);
        if ( c != null ) {
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        return c;
    }


    @Override
    @Transactional
    public ProductItem delete(Long id) {
        ProductItem result = new ProductItem();
        ProductItem c = getHibernateTemplate().get(ProductItem.class, id);
        if ( c != null ) {
            User u = c.getCreatedBy();
            getHibernateTemplate().delete(c);
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        
        return c;
    }

    @Override
    @Transactional(readOnly=true)
    public ProductItem findById(Long id) {
        ProductItem entity = (ProductItem) getHibernateTemplate().get(ProductItem.class, id);
        return entity;
    }
    

    protected DetachedCriteria buildCriteriaByExample(QueryPage<ProductItem> queryPage) {
        ProductItem sample = queryPage.getEntityAsExample();
        Criterion c = CdmsCriteriaExample.createEx(queryPage.getEntityAsExample())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("createdAt")
                .excludeProperty("version");
        DetachedCriteria productItemCr = DetachedCriteria.forClass(ProductItem.class);
        productItemCr.add(c);
        if ( sample.getIdFilter() != null) {
            productItemCr.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        if ( sample.getCreatedAt() != null) {
            if ( sample.getCreatedAtEnd() == null ) {
                sample.setCreatedAtEnd(sample.getCreatedAt());
            }
            productItemCr.add(Restrictions.between("createdAt", sample.getCreatedAt(), sample.getCreatedAtEnd()));
        } else if ( sample.getCreatedAtEnd() != null) {
            productItemCr.add(Restrictions.le("createdAt", sample.getCreatedAtEnd()));
        }
        return productItemCr;
    }
    @Override
    @Transactional(readOnly=true)    
    public QueryPage<ProductItem> findByExample(QueryPage<ProductItem> queryPage) {
        DetachedCriteria productItemCr = buildCriteriaByExample(queryPage);
        ProductItem sample = queryPage.getEntityAsExample();
        
        productItemCr.setProjection(Projections.rowCount());
        List rowCountList = getHibernateTemplate().findByCriteria(productItemCr);
        
        int rowCount = ((Number)rowCountList.get(0)).intValue();
        queryPage.setRowCount(rowCount);
        
        productItemCr = buildCriteriaByExample(queryPage);
        productItemCr.addOrder(Order.asc("id"));
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();

        List<ProductItem> items = (List<ProductItem>) getHibernateTemplate().findByCriteria(productItemCr,firstRec,queryPage.getPageSize());        
        for ( ProductItem item : items) {
            item.getCreatedBy().setPermissions(new ArrayList());
            item.getCreatedBy().setPassword(null);
        }
        queryPage.setQueryResult(items);
        queryPage.setEntityAsExample(null);

        return queryPage;

    }
    
}
