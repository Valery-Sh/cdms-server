/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cdms.entities.Customer;
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
public class CustomerDaoImpl extends HibernateDaoSupport implements CustomerDao{
    
    @Override
    @Transactional
    public Customer insert(Customer customer) {
        User u = getHibernateTemplate().get(User.class,customer.getCreatedBy().getId());
        customer.setCreatedBy(u);
        customer.setCreatedAt(new Date());

        getHibernateTemplate().save(customer);
        getHibernateTemplate().initialize(customer.getCreatedBy());
        return customer;
    }
    
    @Override
    @Transactional
    public Customer update(Customer customer) {
        getHibernateTemplate().get(Customer.class,customer.getId());
        Customer c = getHibernateTemplate().merge(customer);
        if ( c != null ) {
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        return c;
    }


    @Override
    @Transactional
    public void delete(Long id) {
        getHibernateTemplate().delete(id);
    }

    @Override
    @Transactional(readOnly=true)
    public Customer findById(Long id) {
        Customer customer = (Customer) getHibernateTemplate().get(Customer.class, id);
        return customer;
    }
    

    @Override
    @Transactional(readOnly=true)
    public List<Customer> findAll(int start, int pageSize) {
        Customer c = new Customer();
        c.setCreatedBy(new User());
        return findByExample(c, start, pageSize);
    }

    @Override
    @Transactional(readOnly=true)    
    public List<Customer> findByExample(Customer sample,long firstRecordMaxId, int pageSize) {
        Criterion c = CdmsCriteriaExample.createEx(sample)
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("createdAt")
                .excludeProperty("version");
        DetachedCriteria customerCr = DetachedCriteria.forClass(Customer.class);
        customerCr.add(c);
        if ( sample.getIdFilter() != null) {
            customerCr.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        if ( sample.getCreatedAt() != null) {
            if ( sample.getCreatedAtEnd() == null ) {
                sample.setCreatedAtEnd(sample.getCreatedAt());
            }
            customerCr.add(Restrictions.between("createdAt", sample.getCreatedAt(), sample.getCreatedAtEnd()));
        } else if ( sample.getCreatedAtEnd() != null) {
            customerCr.add(Restrictions.le("createdAt", sample.getCreatedAtEnd()));
        }
        customerCr.add(Restrictions.gt("id", firstRecordMaxId));        
        DetachedCriteria userCr = customerCr.createCriteria("createdBy");
        Criterion u = CdmsCriteriaExample.createEx(sample.getCreatedBy())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("version");
        userCr.add(u);
        
        //int firstResult = firstRecordMaxId*pageSize;
        //List<Customer> customers = (List<Customer>) getHibernateTemplate().findByCriteria(customerCr,firstResult,pageSize);
        getHibernateTemplate().setMaxResults(pageSize);
        List<Customer> customers = (List<Customer>) getHibernateTemplate().findByCriteria(customerCr);        
        for ( Customer customer : customers) {
            customer.getCreatedBy().setPermissions(new ArrayList());
            customer.getCreatedBy().setPassword(null);
        }
/*        
  to   
        Long ll;
        
        for ( long i=0; i < 200000000; i++) {
            ll = i * 2 / 3;
        }
        */ 
        return customers;

    }
    protected DetachedCriteria buildCriteriaByExample(QueryPage<Customer> queryPage) {
        Customer sample = queryPage.getEntityAsExample();
        Criterion c = CdmsCriteriaExample.createEx(queryPage.getEntityAsExample())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("createdAt")
                .excludeProperty("version");
        DetachedCriteria customerCr = DetachedCriteria.forClass(Customer.class);
        customerCr.add(c);
        if ( sample.getIdFilter() != null) {
            customerCr.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        if ( sample.getCreatedAt() != null) {
            if ( sample.getCreatedAtEnd() == null ) {
                sample.setCreatedAtEnd(sample.getCreatedAt());
            }
            customerCr.add(Restrictions.between("createdAt", sample.getCreatedAt(), sample.getCreatedAtEnd()));
        } else if ( sample.getCreatedAtEnd() != null) {
            customerCr.add(Restrictions.le("createdAt", sample.getCreatedAtEnd()));
        }

        DetachedCriteria userCr = customerCr.createCriteria("createdBy");
        Criterion u = CdmsCriteriaExample.createEx(sample.getCreatedBy())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("version");
        userCr.add(u);
        

        
        return customerCr;
    }
    @Override
    @Transactional(readOnly=true)    
    public QueryPage<Customer> findByExample(QueryPage<Customer> queryPage) {
        DetachedCriteria customerCr = buildCriteriaByExample(queryPage);
        Customer sample = queryPage.getEntityAsExample();
//        customerCr.add(Restrictions.gt("id", firstRecordMaxId));        
//        getHibernateTemplate().setMaxResults(pageSize);
        
        customerCr.setProjection(Projections.rowCount());
        List rowCountList = getHibernateTemplate().findByCriteria(customerCr);
        
        int rowCount = ((Number)rowCountList.get(0)).intValue();
        queryPage.setRowCount(rowCount);
        
        customerCr = buildCriteriaByExample(queryPage);
        customerCr.addOrder(Order.asc("id"));
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();

        List<Customer> customers = (List<Customer>) getHibernateTemplate().findByCriteria(customerCr,firstRec,queryPage.getPageSize());        
        for ( Customer customer : customers) {
            customer.getCreatedBy().setPermissions(new ArrayList());
            customer.getCreatedBy().setPassword(null);
        }
        queryPage.setQueryResult(customers);
        queryPage.setEntityAsExample(null);
/*        
  to   
        Long ll;
        
        for ( long i=0; i < 200000000; i++) {
            ll = i * 2 / 3;
        }
        */ 
        return queryPage;

    }
    
}
