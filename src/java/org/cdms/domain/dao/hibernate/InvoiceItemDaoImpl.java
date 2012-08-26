package org.cdms.domain.dao.hibernate;

import java.util.List;
import org.cdms.domain.dao.EntityDao;
import org.cdms.shared.entities.Invoice;
import org.cdms.shared.entities.InvoiceItem;
import org.cdms.shared.entities.ProductItem;
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
public class InvoiceItemDaoImpl<E extends InvoiceItem> extends HibernateDaoSupport implements EntityDao<E>{
    /**
     * Inserts the specified entity of type <code>InvoiceItem</code> into the database.
     * @param entity the entity to be inserted. 
     * @return the inserted entity of type <code>InvoiceItem</code>. 
     * @see org.cdms.entities.InvoiceItem
     */
    @Override
    @Transactional
    public E insert(E entity) {
        Invoice invoice = getHibernateTemplate().get(Invoice.class,entity.getInvoice().getId());        
        ProductItem productItem = getHibernateTemplate().get(ProductItem.class,entity.getProductItem().getId());        
        entity.setInvoice(invoice);
        entity.setProductItem(productItem);
        getHibernateTemplate().save(entity);
        return entity;
    }
    /**
     * Updates the specified entity of type <code>InvoiceItem</code>.
     * @param entity the entity to be updates. 
     * @return the updated entity of type <code>InvoiceItem</code>. 
     * @see org.cdms.entities.InvoiceItem
     */
    @Override
    @Transactional
    public E update(E entity) {
        getHibernateTemplate().get(entity.getClass(),entity.getId());
        E c = getHibernateTemplate().merge(entity);
        return c;
    }

    /**
     * Deletes the entity with the specified <code>identifier</code>.
     * @param id the id of the entity to be deleted. 
     * @return the deleted entity of type <code>Invoice</code>. 
     * @see org.cdms.entities.InvoiceItem
     */
    @Override
    @Transactional
    public E delete(Long id) {
        E result = (E)new InvoiceItem();
        E c = (E)getHibernateTemplate().get(InvoiceItem.class, id);
        if ( c != null ) {
            getHibernateTemplate().delete(c);
        }
        
        return c;
    }
    /**
     * Executes query to retrieve the entity with the specified <code>identifier</code>.
     * @param id  the identifier of the entity to search for. 
     * @return the found entity of type <code>InvoiceItem</code>. 
     * @see org.cdms.entities.InvoiceItem
     */
    @Override
    @Transactional(readOnly=true)
    public E findById(Long id) {
        E entity = (E) getHibernateTemplate().get(InvoiceItem.class, id);
        return entity;
    }
    

    protected DetachedCriteria buildCriteriaByExample(QueryPage<E> queryPage) {
        E sample = queryPage.getEntityAsExample();
        Criterion c = CdmsCriteriaExample.createEx(queryPage.getEntityAsExample())
                .enableLike(MatchMode.ANYWHERE)
                .ignoreCase()
                .excludeProperty("id")
                .excludeProperty("idFilter")                
                .excludeProperty("version");
        DetachedCriteria invoiceItemCr = DetachedCriteria.forClass(sample.getClass());
        invoiceItemCr.add(c);
        if ( sample.getIdFilter() != null) {
            invoiceItemCr.add(Restrictions.sqlRestriction("{alias}.id like'%" + sample.getIdFilter() +"%'"));
        }
        return invoiceItemCr;
    }
    /**
     * Executes a query and stores results into the given object 
     * of type <code>QueryPage</code>. based for entities of type <code>InvoiceItem</code> by the given
     * The method builds query using Hibernate Criteria API. 
     * The <code>queryPage</code> contains properties such as <code>pageNo</code>
     * and <code>pageSize</code>. Thus the query retrieves only the records that 
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
     * @see org.cdms.entities.InvoiceItem
     * @see org.cdms.remoting.QueryPage
     */
    @Override
    @Transactional(readOnly=true)    
    public QueryPage<E> findByExample(QueryPage<E> queryPage) {
        DetachedCriteria invoiceItemCr = buildCriteriaByExample(queryPage);
        
        invoiceItemCr.setProjection(Projections.rowCount());
        List rowCountList = getHibernateTemplate().findByCriteria(invoiceItemCr);
        
        int rowCount = ((Number)rowCountList.get(0)).intValue();
        queryPage.setRowCount(rowCount);
        
        invoiceItemCr = buildCriteriaByExample(queryPage);
        invoiceItemCr.addOrder(Order.asc("id"));
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();

        List<E> items = (List<E>) getHibernateTemplate().findByCriteria(invoiceItemCr,firstRec,queryPage.getPageSize());        
        queryPage.setQueryResult(items);
        queryPage.setEntityAsExample(null);

        return queryPage;

    }
    
}
