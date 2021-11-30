package org.datanucleus.datatrail.impl.nodes.converter;

import org.datanucleus.datatrail.impl.nodes.Priority;
import org.datanucleus.datatrail.StringConverter;

/**
 * Null converter.  Returns a null for a null object
 */
@Priority(priority = Priority.LOWEST_PRECEDENCE)
public class NullConverter implements StringConverter {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == null;
    }

    @Override
    public String getAsString(Object value) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NullConverter;
    }

    @Override
    public int hashCode() {
        return NullConverter.class.hashCode();
    }

}
