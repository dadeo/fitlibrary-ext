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
