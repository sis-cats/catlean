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
package fr.ca.cat.catlean.oapp.processor.domain;

import fr.ca.cat.catlean.oapp.processor.core.Cloneable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author lefebvreme
 * @since 26-02-2016
 * @version 0.0.1
 */
public class LogEvent implements Cloneable {

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void cloneTo(Cloneable clonee) throws CloneNotSupportedException, IllegalStateException, IllegalAccessException {
        clone(); // let me know if I'm cloneable.
        Field[] fields = clonee.getClass().getDeclaredFields();
        List<Field> myFields = Arrays.asList(getClass().getDeclaredFields());
        for (final Field f : fields) {
            if (myFields.contains(f)) {
                f.set(this, f.get(clonee));
            }
        }
    }
}
