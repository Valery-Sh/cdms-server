/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import org.cdms.entities.ProductItem;
import org.cdms.remoting.QueryPage;

/**
 *
 * @author Valery
 */
public interface ProductItemDao {
    ProductItem insert(ProductItem entity); 
    ProductItem update(ProductItem entity); 
    ProductItem delete(Long id); 
    ProductItem findById(Long id); 
    QueryPage<ProductItem> findByExample(QueryPage<ProductItem> queryPage);
}
