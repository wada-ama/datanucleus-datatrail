package org.datanucleus.datatrail;

import org.datanucleus.datatrail.impl.nodes.Priority;

/**
 * Identifies a converter for converting a given Class to a String
 * Each converter can specify a {@link Priority} if multiple converters are found for the same class
 * Converters should be stateless and considered as "Singletons" within the context of the DataTrail.  They must be designed to be thread safe.
 *
 * As "Singletons", they should all implement a unique hashcode & equals methods to ensure that multiple instances of the same converter are considered
 * as the same object
 *
 */
public interface StringConverter {

    /**
     * Identifies if the converter can convert the given class
     * @param clazz the class to be converted.  A null value is represented by a {@code null} clazz value
     * @return
     */
    boolean supports(Class<?> clazz);

    /**
     * Returns the object as a String
     * @param value
     * @return
     */
    String getAsString( Object value );


    /**
     * The priority of the converter of the class.  By default, the priority is '0'.
     *
     * @return
     */
    default int priority() {
        final Priority nodePriority = getClass().getAnnotation(Priority.class);
        return nodePriority == null ? 0 : nodePriority.priority();
    }

}
