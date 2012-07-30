package org.cdms;

/**
 *
 * @author V. Shyshkin
 */
public class RemoteAuthenticationException  extends RuntimeException{
    /**
     * A fully qualified class name of the actual exception 
     */
    private String originalClassName;

    public RemoteAuthenticationException(String message) {
        super(message);
    }

    public String getOriginalClassName() {
        return originalClassName;
    }

    public void setOriginalClassName(String originalClassName) {
        this.originalClassName = originalClassName;
    }

}
