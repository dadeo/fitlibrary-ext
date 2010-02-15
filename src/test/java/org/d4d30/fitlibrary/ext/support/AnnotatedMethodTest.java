package org.d4d30.fitlibrary.ext.support;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.d4d30.fitlibrary.ext.support.ClassUtil.findMethod;
import static org.junit.Assert.*;

public class AnnotatedMethodTest {

    @Test
    public void getParameterPositions_side_by_side() {
        int[] positions = new AnnotatedMethod("owner _ _", findMethod("test1")).getParameterPositions();
        assertEquals(2, positions.length);
        assertEquals(0, positions[0]);
        assertEquals(1, positions[1]);
    }
    
    @Test
    public void getParameterPositions_separated() {
        int[] positions = new AnnotatedMethod("first name _ and last name _", findMethod("test1")).getParameterPositions();
        assertEquals(2, positions.length);
        assertEquals(0, positions[0]);
        assertEquals(2, positions[1]);
    }

}
