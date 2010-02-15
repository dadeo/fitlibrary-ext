package org.d4d30.fitlibrary.ext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {
    String[] value();
}
