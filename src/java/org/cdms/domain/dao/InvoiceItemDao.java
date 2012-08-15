/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import org.cdms.entities.InvoiceItem;
import org.cdms.remoting.QueryPage;

/**
 *
 * @author V. Shyshkin
 */
public interface InvoiceItemDao {
    InvoiceItem insert(InvoiceItem entity); 
    InvoiceItem update(InvoiceItem entity); 
    InvoiceItem delete(Long id); 
    InvoiceItem findById(Long id); 
    QueryPage<InvoiceItem> findByExample(QueryPage<InvoiceItem> queryPage);
}
