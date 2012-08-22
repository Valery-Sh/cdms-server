package org.cdms.domain.dao;

/**
 *
 * @author V. Shyshkin
 */
public interface RemoteExceptionHandler {
    
    public void throwDeleteFailure(long id,String entityClassName);
    public void throwDataAccessTranslated(Exception e);
}