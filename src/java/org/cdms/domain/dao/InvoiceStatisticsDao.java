package org.cdms.domain.dao;

import org.cdms.entities.InvoiceStatView;
import org.cdms.remoting.QueryPage;

/**
 *
 * @author V. Shyshkin
 */
public interface InvoiceStatisticsDao {
    QueryPage<InvoiceStatView> requestInvoice(QueryPage<InvoiceStatView> queryPage );
}
