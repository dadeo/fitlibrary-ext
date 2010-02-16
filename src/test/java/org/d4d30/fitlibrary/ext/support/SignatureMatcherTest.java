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

import fitlibrary.table.Row;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SignatureMatcherTest {

    @Test
    public void match_multiple_variables_side_by_side() {
        SignatureMatcher matcher = new SignatureMatcher(Arrays.asList("owner", "_", "_"));

        assertTrue(matcher.matches(new Row("owner", "pinky", "jones")));
        assertTrue(matcher.matches(new Row("owner", "john", "doe")));

        assertFalse(matcher.matches(new Row("name", "pinky", "jones")));
        assertFalse(matcher.matches(new Row("user", "john", "doe")));
    }

    @Test
    public void match_variables_split() {
        SignatureMatcher matcher = new SignatureMatcher(Arrays.asList("part1", "_", "part2", "_"));

        assertTrue(matcher.matches(new Row("part1", "abc", "part2", "def")));
        assertTrue(matcher.matches(new Row("part1", "def", "part2", "abc")));

        assertFalse(matcher.matches(new Row("partX", "abc", "part2", "def")));
        assertFalse(matcher.matches(new Row("part1", "abc", "partX", "def")));
    }

    @Test
    public void match_different_number_of_parts() {
        SignatureMatcher matcher = new SignatureMatcher(Arrays.asList("owner", "_", "_"));
        assertFalse(matcher.matches(new Row("name", "pinky")));
    }

}
