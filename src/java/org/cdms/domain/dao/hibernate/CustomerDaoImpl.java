package org.cdms.domain.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cdms.domain.dao.EntityDao;
import org.cdms.shared.entities.Customer;
import org.cdms.shared.entities.User;
import org.cdms.shared.remoting.QueryPage;
import org.cdms.shared.remoting.exception.RemoteValidationException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides methods to perform the search, insert, update and delete 
 * records from the database using the Hibernate DAO Support API.
 * 
 * @author V. Shyshkin
 */
public class CustomerDaoImpl<E extends Customer> extends HibernateDaoSupport implements EntityDao<E>{
    /**
     * Inserts the specified entity of type <code>Customer</code> into the database.
     * @param entity the entity to be inserted. 
     * @return the inserted entity of type <code>Customer</code>. 
     * @see org.cdms.entities.Customer
     */
    @Override
    @Transactional
    public E insert(E entity) {
        User u = getHibernateTemplate().get(User.class,entity.getCreatedBy().getId());
        entity.setCreatedBy(u);
        entity.setCreatedAt(new Date());

        getHibernateTemplate().save(entity);
        getHibernateTemplate().initialize(entity.getCreatedBy());
        return entity;
    }

    /**
     * Updates the specified entity of type <code>Customer</code>.
     * @param entity the entity to be updates. 
     * @return the updated entity of type <code>Customer</code>. 
     * @see org.cdms.entities.Customer
     */
    @Override
    @Transactional
    public E update(E entity) {
        //getHibernateTemplate().get(Customer.class,customer.getId());
        getHibernateTemplate().get(entity.getClass(),entity.getId());
        E c = getHibernateTemplate().merge(entity);
        if ( c != null ) {
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        return (E)c;
    }

    /**
     * Deletes the entity with the specified <code>identifier</code>.
     * @param id the id of the entity to be deleted. 
     * @return the deleted entity of type <code>Customer</code>. 
     * @see org.cdms.entities.Customer
     */
    @Override
    @Transactional
    public E delete(Long id) {
        E result = (E)new Customer();
        E c = (E)getHibernateTemplate().get(Customer.class, id);
        if ( c != null ) {
            User u = c.getCreatedBy();
            getHibernateTemplate().delete(c);
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        
        return c;
    }
    /**
     * Executes query to retrieve the entity with the specified <code>identifier</code>.
     * @param id  the identifier of the entity to search for. 
     * @return the found entity of type <code>Customer</code>. 
     * @see org.cdms.entities.Customer
     */
    @Override
    @Transactional(readOnly=true)
    public E findById(Long id) {
        E customer = (E) getHibernateTemplate().get(Customer.class, id);
        return customer;
    }
    /**
     * 
     * @param queryPage
     * @return 
     */
    protected DetachedCriteria buildCriteriaByExample(QueryPage<E> queryPage) {
        E sample = queryPage.getEntityAsExample();
        Criterion c = CdmsCriteriaExample.createEx(queryPage.getEntityAsExample())
                .enableLike(MatchMode.ANYWHERE)
                .ignoreCase()
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("createdAt")
                .excludeProperty("version");
        DetachedCriteria customerCr = DetachedCriteria.forClass(sample.getClass());
        customerCr.add(c);
        if ( sample.getIdFilter() != null) {
            customerCr.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        Criterion dc = dateCriterion(sample.getCreatedAt(),sample.getCreatedAtEnd());
        if ( dc != null ) {
            customerCr.add(dc);
        }

        DetachedCriteria userCr = customerCr.createCriteria("createdBy");
        Criterion u = CdmsCriteriaExample.createEx(sample.getCreatedBy())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .excludeProperty("version");
        userCr.add(u);
        

        
        return customerCr;
    }
    private Criterion dateCriterion(Date startDate, Date endDate) {
        Criterion c = null;
        if (startDate != null) {
            if (endDate == null) {
                c = Restrictions.ge("createdAt", startDate);
            } else if (startDate.equals(endDate)){
                c = Restrictions.eq("createdAt", startDate);
            } else if (endDate.compareTo(startDate) < 0 ){
                throw new RemoteValidationException("Start date must be null or equals or less than endDate");
            } else {
                c = Restrictions.between("createdAt", startDate,endDate);
            }
        } else if (endDate != null) {
             c = Restrictions.le("createdAt", endDate);
        }
        return c;
        
    }
    /**
     * Executes a query and stores results into the given object 
     * of type <code>QueryPage</code>. based for entities of type <code>Customer</code> by the given
     * The method builds query using Hibernate Criteria API. 
     * The <code>queryPage</code> contains properties such as <code>pageNo</code>
     * and <code>pageSize</code>. Thus the query retrieves only the record that 
     * correspond to the single page.
     * The result of the query is a collection of type <code>java.util.List</code>.
     * The method stores the results into the <code>queryPage</code>. 
     * In addition to the main result the method retrieves the number of rows
     * and stores it into property <code>rowCount</code> 
     * of the <code>queryPage</code>.
     * 
     * @param queryPage the object that contains the query parameters, paging parameters
     *    and a collection to store query results
     * @return the object of type <code>QueryPage</code>
     * @see org.cdms.entities.Customer
     * @see org.cdms.remoting.QueryPage
     */
    @Override
    @Transactional(readOnly=true)    
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        DetachedCriteria customerCr = buildCriteriaByExample(queryPage);
        
        customerCr.setProjection(Projections.rowCount());
        List rowCountList = getHibernateTemplate().findByCriteria(customerCr);
        
        int rowCount = ((Number)rowCountList.get(0)).intValue();
        queryPage.setRowCount(rowCount);
        
        customerCr = buildCriteriaByExample(queryPage);
        customerCr.addOrder(Order.asc("id"));
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();

        List<E> customers = (List<E>) getHibernateTemplate().findByCriteria(customerCr,firstRec,queryPage.getPageSize());        
        for ( Customer customer : customers) {
            customer.getCreatedBy().setPermissions(new ArrayList());
            customer.getCreatedBy().setPassword(null);
        }
        queryPage.setQueryResult(customers);
        queryPage.setEntityAsExample(null);
        
        return queryPage;

    }

    
}
