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
package org.d4d30.fitlibrary.ext;

import fit.Fixture;

public class ParentAnnotatedDoFixture extends AnnotationDoFixture {

    @Alias({"owner _ _",
            "borrower _ _"})
    public boolean name(String first, String last) {
        System.out.println("<<<<< name first:" + first + " last:" + last);
        return true;
    }

    @Alias("borrower _ applies for loan with loan type id _")
    public boolean method1(String name, String loanTypeId) {
//    public boolean method1(String name) {
        System.out.println("<<<<< name name:" + name);
        return true;
    }

    @Alias("child")
    public Fixture method2() {
        return new ChildAnnotatedDoFixture();
    }

    @Alias("child2")
    public void method3() {
        setSystemUnderTest(new ChildAnnotatedDoFixture());
    }

    @Alias("username")
    public String username() {
        return "pinky"; 
    }
}
