package org.cdms.domain.dao;

import java.util.List;
import org.cdms.entities.Customer;
import org.cdms.remoting.QueryPage;

/**
 *
 * @author V. Shyshkin
 */
public interface CustomerDao {
    Customer insert(Customer user); 
    Customer update(Customer user); 
    void delete(Long id); 
    Customer findById(Long id); 
    List<Customer> findByExample(Customer sample, long firstRecordMaxId,int pageSize);     
    QueryPage<Customer> findByExample(QueryPage<Customer> queryPage);
    List<Customer> findAll(int start, int pageSize); 
    
}
