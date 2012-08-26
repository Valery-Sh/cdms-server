package org.cdms.domain.dao.hibernate;

import org.cdms.domain.dao.RemoteExceptionHandler;
import org.cdms.shared.remoting.exception.RemoteDataAccessException;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate3.HibernateQueryException;
import org.springframework.orm.hibernate3.HibernateSystemException;

/**
 *
 * @author V. Shyshkin
 */
public class RemoteExceptionHandlerImpl implements RemoteExceptionHandler {
    
    @Override
    public void throwDeleteFailure(long id,String entityClassName) {
        RemoteDataAccessException re = new RemoteDataAccessException("attempt to delete non-existend entity");
        re.setErrorCode(RemoteDataAccessException.OBJECT_RETRIEVAL_DELETE);
        re.setIdentifier(id);
        re.setPersistentClassName(entityClassName);
        re.setOriginalClassName(HibernateObjectRetrievalFailureException.class.getName());
        throw re;

    }    
    @Override
    public void throwDataAccessTranslated(Exception e) {
        RemoteDataAccessException re = new RemoteDataAccessException(e.getMessage());
        re.setOriginalClassName(e.getClass().getName());
        if ( e instanceof HibernateObjectRetrievalFailureException ) {
            HibernateObjectRetrievalFailureException ex = (HibernateObjectRetrievalFailureException)e;
            re.setErrorCode(RemoteDataAccessException.OBJECT_RETRIEVAL);
            re.setIdentifier(ex.getIdentifier());
            re.setPersistentClassName(ex.getPersistentClassName());
            if ( ex.getPersistentClass() != null ) {
                re.setEntityName(ex.getPersistentClass().getSimpleName());
            }
        } else if ( e instanceof HibernateQueryException) {
            HibernateQueryException ex = (HibernateQueryException)e;            
            re.setErrorCode(RemoteDataAccessException.QUERY);            
            re.setQueryString(ex.getQueryString());
        } else if ( e instanceof HibernateJdbcException) {
            re.setErrorCode(RemoteDataAccessException.JDBC);            
            HibernateJdbcException ex = (HibernateJdbcException)e;
            re.setQueryString(ex.getSql());
        } else if ( e instanceof HibernateSystemException) {
            re.setErrorCode(RemoteDataAccessException.SYSTEM);            
        } else if ( e instanceof HibernateOptimisticLockingFailureException) {
            HibernateOptimisticLockingFailureException ex = (HibernateOptimisticLockingFailureException)e;
            re.setErrorCode(RemoteDataAccessException.OPTIMISTIC_LOCKING);            
            re.setIdentifier(ex.getIdentifier());
            re.setPersistentClassName(ex.getPersistentClassName());
            if ( ex.getPersistentClass() != null ) {
                re.setEntityName(ex.getPersistentClass().getSimpleName());
            }
        } else {
            re.setErrorCode(RemoteDataAccessException.SYSTEM);
        }
        throw re;
    }
    
/*    public void throwAuthTranslated(Exception e) {
        RemoteAuthenticationException re = new RemoteAuthenticationException(e.getMessage());
        re.setOriginalClassName(e.getClass().getName());
        throw re;
    }
*/    
}