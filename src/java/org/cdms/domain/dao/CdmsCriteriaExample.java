/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.domain.dao;

import org.hibernate.criterion.Example;
import org.hibernate.type.Type;

/**
 * Support for query by example.
 * Introduces a {@link #NOT_NULL_OR_ZERO_OR_EMPTY} of type 
 * <code>PropertySelector</code> implementation that allows
 * exclude string properties that contain  empty values.
 * The class allows create
 * @author V. Shyshkin
 */
public class CdmsCriteriaExample extends Example {
    
    protected static final PropertySelector NOT_NULL_OR_ZERO_OR_EMPTY = new NotNullOrZeroOrEmptyPropertySelector();
    
    protected CdmsCriteriaExample(Object entity, PropertySelector selector) {
        super(entity, selector);
    }

    /**
     * Create a new instance, which doesn't include  
     * <code>null,zero (Numbers)</code> or <code>empty</code> strings.
     *
     * @param entity
     * @return a new instance of <tt>CdmsCriteriaExample</tt>
     */
    public static Example createEx(Object entity) {
        if (entity == null) {
            throw new NullPointerException("null example");
        }
        return new CdmsCriteriaExample(entity,NOT_NULL_OR_ZERO_OR_EMPTY);
    }

    /**
     * Exclude zero-valued properties
     */
    public Example excludeZeroeOrEmpty() {
        setPropertySelector(NOT_NULL_OR_ZERO_OR_EMPTY);
        return this;
    }

    public static final class NotNullOrZeroOrEmptyPropertySelector implements PropertySelector {

        @Override
        public boolean include(Object object, String propertyName, Type type) {
                if ( object == null ) {
                    return false;
                }
                if ( (object instanceof Number) && ((Number) object).longValue() == 0 ) {
                    return false;
                }
                if ( (object instanceof String) && ((String) object).isEmpty() ) {
                    return false;
                }
                return true;
        }

        private Object readResolve() {
            return NOT_NULL_OR_ZERO_OR_EMPTY;
        }
    }
}
