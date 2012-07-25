package org.cdms;

import java.io.Serializable;

/**
 *
 * @author V. Shyshkin
 */
public class RemoteConstraintViolation implements Serializable{
    private Object invalidValue;
    private String constraintMessage;
    private String constraintMessageTemplate;    
    private String propertyPath;

    public RemoteConstraintViolation(Object invalidValue, String constraintMessage, String constraintMessageTemplate, String propertyPath) {
        this.invalidValue = invalidValue;
        this.constraintMessage = constraintMessage;
        this.constraintMessageTemplate = constraintMessageTemplate;
        this.propertyPath = propertyPath;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }

    public String getConstraintMessage() {
        return constraintMessage;
    }

    public void setConstraintMessage(String constraintMessage) {
        this.constraintMessage = constraintMessage;
    }

    public String getConstraintMessageTemplate() {
        return constraintMessageTemplate;
    }

    public void setConstraintMessageTemplate(String constraintMessageTemplate) {
        this.constraintMessageTemplate = constraintMessageTemplate;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }
    

}
