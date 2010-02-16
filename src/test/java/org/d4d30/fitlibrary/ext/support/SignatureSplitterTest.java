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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SignatureSplitterTest {
    
    @Test
    public void split() {
        SignatureSplitter splitter = new SignatureSplitter();
        assertEquals(Arrays.asList("owner", "_", "_"), splitter.split("owner _ _"));
        assertEquals(Arrays.asList("owner", "_", "_"), splitter.split("owner__"));
        assertEquals(Arrays.asList("_", "do something", "_"), splitter.split("_ do something _"));
        assertEquals(Arrays.asList("stuff here", "_", "and here", "_"), splitter.split("stuff here _ and here _"));
        assertEquals(Arrays.asList("stuff here", "_", "and here"), splitter.split("stuff here _ and here"));
        assertEquals(Arrays.asList("_", "_", "stuff here"), splitter.split("_ _ stuff here"));
    }

}
