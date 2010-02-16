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
