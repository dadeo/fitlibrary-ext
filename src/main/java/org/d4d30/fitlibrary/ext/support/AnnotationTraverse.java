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

import fitlibrary.closure.CalledMethodTarget;
import fitlibrary.closure.Closure;
import fitlibrary.exception.method.MissingMethodException;
import fitlibrary.global.PlugBoard;
import fitlibrary.table.Row;
import fitlibrary.traverse.Evaluator;
import fitlibrary.traverse.workflow.DoTraverse;
import fitlibrary.typed.TypedObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnnotationTraverse extends DoTraverse {

    public AnnotationTraverse(Object sut) {
        super(sut);
    }

    @Override
    public CalledMethodTarget findMethodByActionName(Row row, int allArgs) throws Exception {
        CalledMethodTarget target = null;

        try {
            target = PlugBoard.lookupTarget.findMethodInEverySecondCell(this, row, allArgs);
        } catch (MissingMethodException e) {
            Map<TypedObject, List<AnnotatedMethod>> map = new LinkedHashMap<TypedObject, List<AnnotatedMethod>>();
            TypedObject typedObject = this.getTypedSystemUnderTest();
            while (typedObject != null && typedObject.getSubject() != null) {
                map.put(typedObject, new AnnotatedMethodCollector().collectFrom(typedObject.getSubject()));
                typedObject = ((Evaluator) typedObject.getSubject()).getTypedSystemUnderTest();
            }
            
            boolean found = false;
            out:
            for (Map.Entry<TypedObject, List<AnnotatedMethod>> entry : map.entrySet()) {
                for (AnnotatedMethod method : entry.getValue()) {
                    if (method.matches(row)) {
                        Closure closure = entry.getKey().findMethodClosure(method.getName(), method.getParameterCount(), true);
                        target = new AnnotationCalledMethodTarget(closure, this, method.getParameterPositions());
                        found = true;
                        break out;
                    }
                }
            }

            if (!found) {
                throw e;
            }
        }
        return target;
    }

}
