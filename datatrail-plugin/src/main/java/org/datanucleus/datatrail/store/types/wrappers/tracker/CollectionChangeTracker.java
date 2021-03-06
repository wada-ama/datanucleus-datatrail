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
 * Change tracker that can be used for collections. If the user calls
 * any mutating methods on the collection that do not have an equivalent in
 * this change tracker, then you must call {@link #stopTracking} after
 * applying the operation to the collection.
 *
 * @author Abe White
 */
public interface CollectionChangeTracker
    extends ChangeTracker {

    /**
     * Record that the given element was added.
     */
    void added(Object elem);

    /**
     * Record that the given element was removed.
     */
    void removed(Object elem);
}
