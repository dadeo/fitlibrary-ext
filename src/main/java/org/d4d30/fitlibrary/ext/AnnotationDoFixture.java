package org.d4d30.fitlibrary.ext;

import fitlibrary.DoFixture;
import org.d4d30.fitlibrary.ext.support.AnnotationTraverse;

public class AnnotationDoFixture extends DoFixture {

    public AnnotationDoFixture() {
        setTraverse(new AnnotationTraverse(this));
    }

}
