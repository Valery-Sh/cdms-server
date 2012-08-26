package org.cdms.remoting.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.cdms.domain.dao.InvoiceStatisticsDao;
import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.shared.entities.InvoiceStatView;
import org.cdms.shared.remoting.InvoiceStatisticsService;
import org.cdms.shared.remoting.QueryPage;
import org.cdms.shared.remoting.exception.RemoteValidationException;

/**
 *
 * @author V. Shyshkin
 */
public class InvoiceStatisticsServiceImpl implements InvoiceStatisticsService {
    
    private InvoiceStatisticsDao statisticsDao;

    private RemoteExceptionHandler exceptionHandler;
    
    public InvoiceStatisticsServiceImpl() {
    }

    public void setInvoiceStatisticsDao(InvoiceStatisticsDao dao) {
        this.statisticsDao = dao;
    }


    public void setExceptionHandler(RemoteExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public QueryPage<InvoiceStatView> requestInvoice(QueryPage<InvoiceStatView> queryPage) {
        QueryPage result = null;
        //QueryPage r = queryPage;
        try {
            result = statisticsDao.requestInvoice(queryPage);
        } catch(Exception e) {
            if ( e instanceof RemoteValidationException ) {
                throw (RemoteValidationException)e;
            }
            
            exceptionHandler.throwDataAccessTranslated(e);        
        }
        if ( result != null && result.getQueryResult() != null && !result.getQueryResult().isEmpty() ) {
            List r = result.getQueryResult();
            List l = new ArrayList(r.size());
            for ( int i=0; i < r.size(); i++) {
                Object[] a = (Object[])r.get(i);
                // convert BigDecimal to String due to Hessian error 
                String sd = ((BigDecimal)a[2]).toPlainString();
                
                InvoiceStatView v = new InvoiceStatView((String)a[0], (Long)a[1],(BigDecimal)a[2],sd);
                l.add(v);
            }
            result.setQueryResult(l);
        }
        
        return result;

    }


}