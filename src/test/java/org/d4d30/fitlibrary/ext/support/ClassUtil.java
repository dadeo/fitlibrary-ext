package org.d4d30.fitlibrary.ext.support;

import java.lang.reflect.Method;

public class ClassUtil {


    public static Method findMethod(String methodName) {
        for (Method method : AnnotatedThing.class.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new RuntimeException(methodName + " was not found");
    }

}
