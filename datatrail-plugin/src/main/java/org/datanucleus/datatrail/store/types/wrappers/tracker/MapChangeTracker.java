/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.datanucleus.datatrail.store.types.wrappers.tracker;

/**
 * Change tracker that can be used for maps. If the user calls
 * any mutating methods on the map that do not have an equivalent in
 * this change tracker, then you must call {@link mydomain.datanucleus.type.wrappers.tracker.ChangeTracker#stopTracking}
 * after applying the operation to the map. The collections returned from
 * {@link mydomain.datanucleus.type.wrappers.tracker.ChangeTracker#getAdded} and {@link mydomain.datanucleus.type.wrappers.tracker.ChangeTracker#getRemoved} will
 * be collections of keys to add/remove.
 *
 * @author Abe White
 */
public interface MapChangeTracker
    extends ChangeTracker {

    /**
     * Whether to track keys or values. Defaults to keys.
     * If you set to values, it is assumed there is a 1-1 correlation
     * between keys and values in this map.
     */
    boolean getTrackKeys();

    /**
     * Whether to track keys or values. Defaults to keys.
     * If you set to values, it is assumed there is a 1-1 correlation
     * between keys and values in this map.
     */
    void setTrackKeys(boolean keys);

    /**
     * Record that the given entry was added to the map.
     */
    void added(Object key, Object val);

    /**
     * Record that the given entry was removed from the map.
     */
    void removed(Object key, Object val);

    /**
     * Record that the given entry was altered.
     */
    void changed(Object key, Object oldVal, Object newVal);
}
