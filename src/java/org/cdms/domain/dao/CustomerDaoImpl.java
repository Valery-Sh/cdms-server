/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import java.util.ArrayList;
import java.util.List;
import org.cdms.entities.Customer;
import org.cdms.entities.Permission;
import org.cdms.entities.PropertyFilter;
import org.cdms.entities.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
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
    public void insert(Customer customer) {
        getHibernateTemplate().save(customer);
    }
    
    @Override
    @Transactional
    public void update(Customer customer) {
        getHibernateTemplate().get(Customer.class,customer.getId());
        getHibernateTemplate().merge(customer);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        //User user = (User)getHibernateTemplate()..get(User.class,id);
        getHibernateTemplate().delete(id);
    }

    @Override
    @Transactional(readOnly=true)
    public Customer findById(Long id) {
        Customer customer = (Customer) getHibernateTemplate().get(Customer.class, id);
        if ( customer != null ) {
        }
        return customer;
    }
    
    @Override
    @Transactional(readOnly=true)
    public List<Customer> findAll() {
        //PageListHolder p;
        //getHibernateTemplate().f
        //Criteria c = new DetachedCriteria();
        //Session s;
        //DetachedCriteria d;
        return null;
        
    }

    @Override
    public List<Customer> findByExample(Customer sample,int start, int pageSize) {
        Criterion c = CdmsCriteriaExample.createEx(sample)
                .enableLike(MatchMode.ANYWHERE)
//                .excludeZeroes()
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("createdAt")
                .excludeProperty("version");
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
        
        dc.add(c);
        // TODO instead of just getId() we must take into acount the leading zeros ?
        if ( sample.getIdFilter() != null) {
            dc.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        
        
        
       // dc.add(Restrictions.between("createdAt", sample.getCreatedAt(), sample.getCreatedAtEnd()));
        
        int firstResult = start*pageSize;
        List<Customer> customers = (List<Customer>) getHibernateTemplate().findByCriteria(dc,firstResult,pageSize);
        for ( Customer customer : customers) {
            customer.getCreatedBy().setPermissions(new ArrayList());
            customer.getCreatedBy().setPassword(null);
        }
/*        Long ll;
        
        for ( long i=0; i < 200000000; i++) {
            ll = i * 2 / 3;
        }
        */ 
        return customers;

    }
    
}
