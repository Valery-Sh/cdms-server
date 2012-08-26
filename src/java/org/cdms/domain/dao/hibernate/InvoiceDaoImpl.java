package org.cdms.domain.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cdms.domain.dao.EntityDao;
import org.cdms.shared.entities.Invoice;
import org.cdms.shared.entities.User;
import org.cdms.shared.remoting.QueryPage;
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
public class InvoiceDaoImpl<E extends Invoice> extends HibernateDaoSupport implements EntityDao<E>{
    /**
     * Inserts the specified entity of type <code>Invoice</code> into the database.
     * @param entity the entity to be inserted. 
     * @return the inserted entity of type <code>Invoice</code>. 
     * @see org.cdms.entities.Invoice
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
     * Updates the specified entity of type <code>Invoice</code>.
     * @param entity the entity to be updates. 
     * @return the updated entity of type <code>Invoice</code>. 
     * @see org.cdms.entities.Invoice
     */
    @Override
    @Transactional
    public E update(E entity) {
        getHibernateTemplate().get(entity.getClass(),entity.getId());
        E c = getHibernateTemplate().merge(entity);
        if ( c != null ) {
            getHibernateTemplate().initialize(c.getCreatedBy());
        }
        return c;
    }

    /**
     * Deletes the entity with the specified <code>identifier</code>.
     * @param id the id of the entity to be deleted. 
     * @return the deleted entity of type <code>Invoice</code>. 
     * @see org.cdms.entities.Invoice
     */
    @Override
    @Transactional
    public E delete(Long id) {
        E result = (E)new Invoice();
        E c = (E)getHibernateTemplate().get(Invoice.class, id);
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
     * @return the found entity of type <code>Invoice</code>. 
     * @see org.cdms.entities.Invoice
     */
    @Override
    @Transactional(readOnly=true)
    public E findById(Long id) {
        E entity = (E) getHibernateTemplate().get(Invoice.class, id);
        return entity;
    }

    protected DetachedCriteria buildCriteriaByExample(QueryPage<E> queryPage) {
        E sample = queryPage.getEntityAsExample();
        Criterion c = CdmsCriteriaExample.createEx(queryPage.getEntityAsExample())
                .enableLike(MatchMode.ANYWHERE)
                .ignoreCase()
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("customer") 
                .excludeProperty("invoiceItems")                                
                .excludeProperty("createdAt")
                .excludeProperty("version");
        
        DetachedCriteria mainCriteria = DetachedCriteria.forClass(sample.getClass());
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
                .ignoreCase()
                .excludeProperty("id")
                .excludeProperty("version")
                .excludeProperty("permissions");
        
        userCr.add(u);
        
        DetachedCriteria customerCr = mainCriteria.createCriteria("customer");
        Criterion cust = CdmsCriteriaExample.createEx(sample.getCustomer())
                .enableLike(MatchMode.ANYWHERE)
                .excludeProperty("id")
                .ignoreCase()
                .excludeProperty("createdBy")
                .excludeProperty("createdAt")
                .excludeProperty("version");
        
        customerCr.add(cust);

        
        return mainCriteria;
    }
    /**
     * Executes a query and stores results into the given object 
     * of type <code>QueryPage</code>. based for entities of type <code>Invoice</code> by the given
     * The method builds query using Hibernate Criteria API. 
     * The queryPage contains properties such as <code>pageNo</code>
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
     * @see org.cdms.entities.Invoice
     * @see org.cdms.remoting.QueryPage
     */
    @Override
    @Transactional(readOnly=true)    
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        DetachedCriteria mainCriteria = buildCriteriaByExample(queryPage);
        Invoice sample = queryPage.getEntityAsExample();
        
        mainCriteria.setProjection(Projections.rowCount());
        List rowCountList = getHibernateTemplate().findByCriteria(mainCriteria);
        
        int rowCount = ((Number)rowCountList.get(0)).intValue();
        queryPage.setRowCount(rowCount);
        
        mainCriteria = buildCriteriaByExample(queryPage);
        mainCriteria.addOrder(Order.asc("id"));
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();

        List<E> entities = (List<E>) getHibernateTemplate().findByCriteria(mainCriteria,firstRec,queryPage.getPageSize());        
        List<E> list = new ArrayList<E>();
        list.addAll(entities);
        for ( E entity : list) {
            getHibernateTemplate().initialize(entity.getInvoiceItems());
            getHibernateTemplate().initialize(entity.getCustomer().getCreatedBy().getPermissions());            
            getHibernateTemplate().initialize(entity.getCreatedBy().getPermissions());                        
        }            
        queryPage.setQueryResult(list);
        queryPage.setEntityAsExample(null);
        return queryPage;

    }

    
}
