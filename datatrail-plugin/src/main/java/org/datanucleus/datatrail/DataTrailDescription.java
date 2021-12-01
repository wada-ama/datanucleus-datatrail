package org.datanucleus.datatrail;

/**
 * Interface used to identify the description to be generated in the data trail.
 * Each model object can choose to implement the description in a way that makes buisness sense for it.
 */
public interface DataTrailDescription {

    /**
     * Returns a context-sensitive description for the current object/model
     * @return
     */
    String getDataTrailDescription();
}
