package org.cdms;

/**
 * The class may be used to notify remote clients of the exceptions raised
 * on a remote server.
 * Suppose the remote client is a pure swing applications that knows
 * nothing of <code>Spring | Hibernate | JPA</code>. The application
 * communicates with a remote Spring-based server through Hessian
 * (or any other) RMI protocol. When the client 
 * invokes a remote method then an exceptions may occur on the server.
 * The server application translates the exception and transfers 
 * to the client an object of type <code>RemoteDataAccessException</code>
 * instead of the <i>native</i> one.
 * 
 * @author V. Shyshkin
 */
public class RemoteDataAccessException extends RuntimeException{
    /**
     * A fully qualified class name of the actual exception 
     */
    private String originalClassName;
    // for HibernateObjectRetrievalFailureException
    private Object identifier;
    private String persistentClassName;
    private String entityName;
    // for HibernateQueryException & HibernateJdbcException.getSql()
    private String queryString;

    public RemoteDataAccessException(String message) {
        super(message);
    }

    public String getOriginalClassName() {
        return originalClassName;
    }

    public void setOriginalClassName(String originalClassName) {
        this.originalClassName = originalClassName;
    }

    public Object getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    public String getPersistentClassName() {
        return persistentClassName;
    }

    public void setPersistentClassName(String persistentClassName) {
        this.persistentClassName = persistentClassName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
}
