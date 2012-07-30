package org.cdms;

import org.springframework.orm.hibernate3.HibernateJdbcException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate3.HibernateQueryException;
import org.springframework.orm.hibernate3.HibernateSystemException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author V. Shyshkin
 */
public class RemoteExceptionHandler {
    
    public void throwDataAccessTranslated(Exception e) {
        RemoteDataAccessException re = new RemoteDataAccessException(e.getMessage());
        re.setOriginalClassName(e.getClass().getName());
        if ( e instanceof HibernateObjectRetrievalFailureException ) {
            HibernateObjectRetrievalFailureException ex = (HibernateObjectRetrievalFailureException)e;
            re.setIdentifier(ex.getIdentifier());
            re.setPersistentClassName(ex.getPersistentClassName());
            if ( ex.getPersistentClass() != null ) {
                re.setEntityName(ex.getPersistentClass().getSimpleName());
            }
        } else if ( e instanceof HibernateQueryException) {
            HibernateQueryException ex = (HibernateQueryException)e;            
            re.setQueryString(ex.getQueryString());
        } else if ( e instanceof HibernateJdbcException) {
            HibernateJdbcException ex = (HibernateJdbcException)e;
            re.setQueryString(ex.getSql());
        } else if ( e instanceof HibernateSystemException) {
            
        } else if ( e instanceof HibernateOptimisticLockingFailureException) {
            HibernateOptimisticLockingFailureException ex = (HibernateOptimisticLockingFailureException)e;
            re.setIdentifier(ex.getIdentifier());
            re.setPersistentClassName(ex.getPersistentClassName());
            if ( ex.getPersistentClass() != null ) {
                re.setEntityName(ex.getPersistentClass().getSimpleName());
            }
        } else {
            // TODO
        }
        throw re;
    }
    
    public void throwAuthTranslated(Exception e) {
        RemoteAuthenticationException re = new RemoteAuthenticationException(e.getMessage());
        re.setOriginalClassName(e.getClass().getName());
        throw re;
    }
    
}
