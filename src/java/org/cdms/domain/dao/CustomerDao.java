package org.cdms.domain.dao;

import java.util.List;
import org.cdms.entities.Customer;

/**
 *
 * @author V. Shyshkin
 */
public interface CustomerDao {
    Customer insert(Customer user); 
    Customer update(Customer user); 
    void delete(Long id); 
    Customer findById(Long id); 
    List<Customer> findByExample(Customer sample, int start,int pageSize);     
    List<Customer> findAll(int start, int pageSize); 
    
}
