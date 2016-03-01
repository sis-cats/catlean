/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ca.cat.catlean.oapp.processor.core;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public interface Cloneable extends java.lang.Cloneable {

    /**
     * Visitable cloner. For lazy guy as I am, it allow to pass an object
     * into the current one an let this copy each relevant fields into the caller.
     *
     * @param clonee        the caller which wanna stole part of the current state of the object
     * @throws CloneNotSupportedException   leave if clone method isn't supported
     * @throws IllegalStateException        leave if the state of the object isn't consistent
     */
    void cloneTo(Cloneable clonee) throws CloneNotSupportedException, IllegalStateException, IllegalAccessException;
}
