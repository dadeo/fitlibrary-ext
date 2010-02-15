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
            List<AnnotatedMethod> methods = new AnnotatedMethodCollector().collectFrom(this.getSystemUnderTest());
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
