package org.d4d30.fitlibrary.ext.support;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.d4d30.fitlibrary.ext.support.ClassUtil.findMethod;
import static org.junit.Assert.*;

public class AnnotatedMethodCollectorTest {

    @Test
    public void collectFrom_object_annotated() {
        List<AnnotatedMethod> methods = new AnnotatedMethodCollector().collectFrom(new AnnotatedThing());
        assertEquals(3, methods.size());
        assertTrue(methods.toString(), methods.contains(new AnnotatedMethod("owner _ _", findMethod("test1"))));
        assertTrue(methods.toString(), methods.contains(new AnnotatedMethod("user has first name _ and last name _", findMethod("test1"))));
        assertTrue(methods.toString(), methods.contains(new AnnotatedMethod("user is registered for _", findMethod("test2"))));
    }

    @Test
    public void collectFrom_object_not_annotated() {
        List<AnnotatedMethod> methods = new AnnotatedMethodCollector().collectFrom(new NonAnnotatedThing());
        assertEquals(0, methods.size());
    }

}
