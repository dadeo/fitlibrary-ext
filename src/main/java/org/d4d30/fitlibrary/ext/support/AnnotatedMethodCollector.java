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
