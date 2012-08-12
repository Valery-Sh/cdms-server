package org.cdms.domain.dao;

import java.util.List;
import org.cdms.entities.Invoice;
import org.cdms.remoting.QueryPage;

/**
 *
 * @author V. Shyshkin
 */
public interface InvoiceDao {
    Invoice insert(Invoice entity); 
    Invoice update(Invoice entity); 
    Invoice delete(Long id); 
    Invoice findById(Long id); 
    QueryPage<Invoice> findByExample(QueryPage<Invoice> queryPage);
    
}
