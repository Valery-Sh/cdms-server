/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.cdms.domain.dao.InvoiceStatisticsDao;
import org.cdms.shared.entities.InvoiceStatView;
import org.cdms.shared.remoting.QueryPage;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Valery
 */
public class InvoiceStatisticsDaoImpl extends HibernateDaoSupport implements InvoiceStatisticsDao {
    @Override
    @Transactional(readOnly = true)
    public QueryPage<InvoiceStatView> requestInvoice(QueryPage<InvoiceStatView> queryPage) {
        String whereCond = ""; // empty string when both startDate and endDate 
                               // are equal to nullused 
        Long customerId = (Long)queryPage.getParams()[0];
        Date startDate = (Date)queryPage.getParams()[1];
        Date endDate   = (Date)queryPage.getParams()[2];     
        
        Date[] params = new Date[] {};
        
        String clientCond = "";
        if ( customerId != null ) {
            clientCond = " and item.invoice.customer.id=:customerId";
        }
        
        if (startDate != null) {
            if (endDate == null) {
                whereCond = " where item.invoice.createdAt >= :p0";  
                params = new Date[] {startDate};
            } else if (startDate.equals(endDate)){
                whereCond = " where item.invoice.createdAt = :p0 ";  
                params = new Date[] {startDate};
            } else if (endDate.compareTo(startDate) < 0 ){
                queryPage.setQueryResult(new ArrayList());
                queryPage.setRowCount(0);
                return queryPage;
            } else {
                whereCond = " where item.invoice.createdAt between :p0 and :p1 ";  
                params = new Date[] {startDate, endDate};
            }
        } else if (endDate != null) {
            whereCond = " where item.invoice.createdAt <= :p0";  
            params = new Date[] {endDate};
        } else if ( customerId != null) {
            clientCond = " item.invoice.customer.id=:customerId";
            whereCond = " where ";
        }
        whereCond += clientCond;
        
        String groupBy = " group by item.productItem.itemName ";
        String orderBy = " order by upper(itemName) ";        
        
        String select = "select upper(item.productItem.itemName) as itemName,"
                + "sum(item.itemCount) as amount,  " 
                + "sum(item.itemCount * item.productItem.price) as totals "
                + "   from InvoiceItem as item ";
        //
        // First, calculate the whole number of rows for the specified where condition
        //
        String queryString = select + whereCond + groupBy + orderBy;
        Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryString);        
        if ( customerId != null) {
            q.setLong("customerId", customerId);
        }
        if ( params.length == 2 ) {
            q.setDate("p0", params[0]);
            q.setDate("p1", params[1]);
        } else if ( params.length == 1 ) {
            q.setDate("p0", params[0]);
        }
        
        Iterator it = q.iterate();
        long rowCount = 0;
        while ( it.hasNext() ) {
            it.next();
            rowCount++;
        }
        queryPage.setRowCount(rowCount);
        if ( rowCount == 0 )  {
            queryPage.setQueryResult(new ArrayList());
            return queryPage;
        }
        //
        // Load results
        //
        queryString = select + whereCond + groupBy + orderBy;
        q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryString);        
        if ( customerId != null) {
            q.setLong("customerId", customerId);
        }
        if ( params.length == 2 ) {
            q.setDate("p0", params[0]);
            q.setDate("p1", params[1]);
        } else if ( params.length == 1 ) {
            q.setDate("p0", params[0]);
        }
        
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();
        q.setFirstResult(firstRec);
        q.setMaxResults(queryPage.getPageSize());
        
        List list = q.list();
        
        queryPage.setQueryResult(list);
        
        return queryPage;
    }             

    
//    @Override
/*    @Transactional(readOnly = true)
    public QueryPage<InvoiceStatView> requestInvoiceOld(QueryPage<InvoiceStatView> queryPage) {
        String whereCondCount = ""; // to calculate row count
        String whereCond = "";
        Long customerId = (Long)queryPage.getParams()[0];
        Date startDate = (Date)queryPage.getParams()[1];
        Date endDate   = (Date)queryPage.getParams()[2];     
        Date q_endDate = endDate;
        Date[] params = new Date[] {startDate, endDate};
        String clientCond = "";
        String clientCondCount = "";        
        if ( customerId != null ) {
            clientCond = " and item.invoice.customer.id=:customerId";
            clientCondCount = " and inv.customer.id=:customerId";
        }
        
        if (startDate != null) {
            if (endDate == null) {
                q_endDate = startDate; 
            }
            whereCond = " where item.invoice.createdAt between :p0 and :p1 ";  
            whereCondCount = " where inv.createdAt between :p0 and :p1";              
            params[1] = q_endDate;
        } else if (endDate != null) {
            whereCond = " where item.invoice.createdAt <= :p0";  
            whereCondCount = " where inv.createdAt <= :p0";  
            params = new Date[] {q_endDate};
        }
        whereCond += clientCond;
        whereCondCount += clientCondCount;
        
        String groupBy = " group by item.productItem.itemName ";
        //String orderBy = " order by item.productItem.itemName ";        
        String orderBy = " order by upper(itemName) ";        
        
        String select = "select upper(item.productItem.itemName) as itemName,"
                + "sum(item.itemCount) as amount,  " 
                + "sum(item.itemCount * item.productItem.price)"
                + "   from InvoiceItem as item ";
        //
        // First, calculate the whole number of rows for the specified where condition
        //
        String queryString = "select count(*) from Invoice inv left join inv.invoiceItems " + whereCondCount ;
        Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryString);        
        
        if ( customerId != null) {
            q.setLong("customerId", customerId);
        }
        
        if ( params.length == 2 ) {
            q.setDate("p0", params[0]);
            q.setDate("p1", params[1]);
            
        } else {
            q.setDate("p0", params[0]);
        }
        
        List list = q.list();
        long rowCount = (Long)list.get(0);
        queryString = select + whereCond + groupBy + orderBy;
        q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryString);        
        if ( customerId != null) {
            q.setLong("customerId", customerId);
        }
        if ( params.length == 2 ) {
            q.setDate("p0", params[0]);
            q.setDate("p1", params[1]);
        } else {
            q.setDate("p0", params[0]);
        }
        
        Iterator it = q.iterate();
        int rowCount1 = 0;
        while ( it.hasNext() ) {
            it.next();
            rowCount1++;
        }
        
        //
        // Load result
        //
        int firstRec = queryPage.getPageNo() * queryPage.getPageSize();
        q.setFirstResult(firstRec);
        q.setMaxResults(queryPage.getPageSize());
        
        list = q.list();
        
        queryPage.setQueryResult(list);
        queryPage.setRowCount(rowCount);
        
        return queryPage;
    }             
*/

}
