package org.cdms.domain.dao;

import java.util.List;
import org.cdms.entities.Customer;
import org.cdms.remoting.QueryPage;

/**
 *
 * @author V. Shyshkin
 */
public interface CustomerDao {
    Customer insert(Customer entity); 
    Customer update(Customer entity); 
    Customer delete(Long id); 
    Customer findById(Long id); 
    List<Customer> findByExample(Customer sample, long firstRecordMaxId,int pageSize);     
    QueryPage<Customer> findByExample(QueryPage<Customer> queryPage);
    List<Customer> findAll(int start, int pageSize); 
    
}
