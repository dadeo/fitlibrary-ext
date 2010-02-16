/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.d4d30.fitlibrary.ext.support;

import org.d4d30.fitlibrary.ext.Alias;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedMethodCollector {

    public List<AnnotatedMethod> collectFrom(Object thing) {
        ArrayList<AnnotatedMethod> result = new ArrayList<AnnotatedMethod>();
        for (Method method : thing.getClass().getMethods()) {
            addAnnotatedMethodsFor(result, method);
        }
        return result;
    }

    private void addAnnotatedMethodsFor(List<AnnotatedMethod> result, Method method) {
        Alias annotation = method.getAnnotation(Alias.class);
        if(annotation != null) {
            for (String value : annotation.value()) {
                result.add(new AnnotatedMethod(value, method));
            }
        }
    }

}
