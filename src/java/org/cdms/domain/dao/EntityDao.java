package org.cdms.domain.dao;

import org.cdms.shared.remoting.QueryPage;

/**
 * Defines common functionality for entity classes.
 * 
 * 
 * @author V. Shyshkin
 * 
 */
public interface EntityDao<E> {
    E insert(E entity); 
    E update(E entity); 
    E delete(Long id); 
    E findById(Long id); 
    QueryPage<E> findByExample(QueryPage<E> queryPage);
}
