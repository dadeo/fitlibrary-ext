package org.d4d30.fitlibrary.ext;

public class ChildAnnotatedDoFixture extends AnnotationDoFixture {
    @Alias("do something")
    public boolean method0() {
        System.out.println("<<<<< child:do something");        
        return true;
    }
}
