package org.d4d30.fitlibrary.ext.support;

import org.d4d30.fitlibrary.ext.Alias;

public class AnnotatedThing {

    public boolean test0(String first, String last) { return true; }

    @Alias({"owner _ _",
            "user has first name _ and last name _"})
    public boolean test1(String first, String last) { return true; }

    @Alias("user is registered for _")
    public boolean test2(String className) { return true; }

    public boolean test3(String className) { return true; }
}
