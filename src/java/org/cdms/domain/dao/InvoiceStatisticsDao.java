package org.cdms.domain.dao;

import org.cdms.shared.entities.InvoiceStatView;
import org.cdms.shared.remoting.QueryPage;

/**
 *
 * @author V. Shyshkin
 */
public interface InvoiceStatisticsDao {
    QueryPage<InvoiceStatView> requestInvoice(QueryPage<InvoiceStatView> queryPage );
}
