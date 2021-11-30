package org.datanucleus.datatrail.impl.nodes.converter;

import org.datanucleus.datatrail.impl.nodes.Priority;
import org.datanucleus.datatrail.StringConverter;

/**
 * Default converter.  Can convert any object by using the toString() method of the object
 */
@Priority(priority = Priority.LOWEST_PRECEDENCE)
public class ObjectConverter implements StringConverter {
    @Override
    public boolean supports(Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    public String getAsString(Object value) {
        return value == null ? null : value.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ObjectConverter;
    }

    @Override
    public int hashCode() {
        return ObjectConverter.class.hashCode();
    }
}
