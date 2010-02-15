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
