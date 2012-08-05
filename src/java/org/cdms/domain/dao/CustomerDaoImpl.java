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
    
/*    protected Customer findByPattern(PropertyFilter[] filters, boolean b) {
        //String whereExpr + "from Customer where " 
        String s = "";
        
        for ( PropertyFilter p : filters) {
            if ( ! s.isEmpty() ) {
                s += " and ";
            }
            s +=  createCompare(p);
        }
        String sql = "from Customer where " + s; 
        List<Customer> customerList = (List<Customer>) getHibernateTemplate().find(sql);
        Customer customer = null;
        if ( customerList != null && ! customerList.isEmpty() ) {
            customer = customerList.get(0);
        }

        return customer;
        
        
    }    
    
    @Override
    public Customer findByPattern(PropertyFilter[] filters) {
        boolean allowCriteria = true;
        for ( PropertyFilter p : filters) {
            if ( ! p.isStringProperty()) {
                //allowCriteria = false;
                break;
            }
        }
        if ( ! allowCriteria ) {
            return findByPattern(filters, true);
        }
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
        for ( PropertyFilter p : filters) {
            dc.add(create(p));
        }

        List<Customer> customerList = (List<Customer>) getHibernateTemplate().findByCriteria(dc);
        Customer customer = null;
        if ( customerList != null && ! customerList.isEmpty() ) {
            customer = customerList.get(0);
        }
        return customer;
    }


    protected Criterion create(PropertyFilter f ) {
        Criterion c = null;
        String cop = f.getOpName().toLowerCase();
        if ( "=".equals(cop)) {
            c = Restrictions.eq(f.getFieldName(), f.getValue());
        } else if ( "like".equals(cop)) {
            //c = Restrictions.like(f.getFieldName(), f.getValue().toString(),MatchMode.ANYWHERE);
            //c = new SimpleExpressionEx(f.getFieldName(), f.getValue().toString());
            c = Restrictions.sqlRestriction("{alias}.id like'%10%'");
        }
        
        return c;
    }

    protected String createCompare(PropertyFilter f ) {
        String c = "";
        String cop = f.getOpName().toLowerCase();
        if ( "=".equals(cop)) {
            c = f.getFieldName() + "=" + f.getValue();
        } else if ( "like".equals(cop)) {
            c = f.getFieldName() + " like '%" + f.getValue() + "%'";
        }
        
        return c;
    }
    
*/    
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
        Criterion c = Example.create(sample)
                .enableLike(MatchMode.ANYWHERE)
                .excludeZeroes()
                .excludeProperty("id")
                .excludeProperty("createdAt")
                .excludeProperty("version");
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);

        // TODO instead of just getId() we must take into acount the leading zeros ?
        dc.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getId() +"%'"));
        dc.add(Restrictions.between("createdAt", sample.getCreatedAt(), sample.getCreatedAtEnd()));
        
        int firstResult = start*pageSize;
        List<Customer> customers = (List<Customer>) getHibernateTemplate().findByCriteria(dc,firstResult,pageSize);
        for ( Customer customer : customers) {
            customer.getCreatedBy().setPermissions(new ArrayList());
            customer.getCreatedBy().setPassword(null);
        }
        
        return customers;

    }
    
}
