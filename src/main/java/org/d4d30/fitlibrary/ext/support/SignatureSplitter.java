package org.d4d30.fitlibrary.ext.support;

import java.util.ArrayList;
import java.util.List;

public class SignatureSplitter {

    public List<String> split(String signature) {
        List<String> result = new ArrayList<String>();
        int pos = signature.indexOf("_");
        int lastpos = 0;
        while(pos != -1) {
            String part = signature.substring(lastpos, pos).trim();
            if(!part.equals("")) {
                result.add(part);
            }
            result.add("_");
            lastpos = pos + 1;
            pos = signature.indexOf("_", lastpos);
        }
        if(lastpos < signature.length()) {
            result.add(signature.substring(lastpos).trim());
        }
        return result;
    }
    
}
