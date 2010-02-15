package org.d4d30.fitlibrary.ext;

import fit.Fixture;

public class TestAnnotatedDoFixture extends AnnotationDoFixture {

    @Alias({"owner _ _",
            "borrower _ _"})
    public boolean name(String first, String last) {
        System.out.println("<<<<< name first:" + first + " last:" + last);
        return true;
    }

    @Alias("borrower _ applies for loan with loan type id _")
    public boolean method1(String name, String loanTypeId) {
        System.out.println("<<<<< name name:" + name + " loanTypeId:" + loanTypeId);
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
