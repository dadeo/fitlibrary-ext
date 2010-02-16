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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedMethod {
    private String signature;
    private Method method;
    private SignatureMatcher matcher;
    private List<String> splitSignature;

    public AnnotatedMethod(String signature, Method method) {
        this.signature = signature;
        this.method = method;
        splitSignature = new SignatureSplitter().split(signature);
        this.matcher = new SignatureMatcher(splitSignature);
    }

    public boolean matches(Row row) {
        boolean matches = matcher.matches(row);
        if(matches) {
            verifyParameterCount();            
        }
        return matches;
    }

    public String getName() {
        return method.getName();
    }

    public int getParameterCount() {
        return method.getParameterTypes().length;
    }

    public int[] getParameterPositions() {
        int[] result = new int[getParameterCount()];
        int index = 0;
        for(int i = 0; i < splitSignature.size(); ++i) {
            if(splitSignature.get(i).equals("_")) {
                result[index] = i - 1;
                ++index;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotatedMethod that = (AnnotatedMethod) o;

        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = signature != null ? signature.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnnotatedMethod{" +
                "signature='" + signature + '\'' +
                ", method=" + method +
                '}';
    }

    private void verifyParameterCount() {
        int signatureParameterCount = 0;
        for(String part : splitSignature) {
            if("_".equals(part)) {
                ++signatureParameterCount;
            }
        }
        if(signatureParameterCount != getParameterCount()) {
            throw new RuntimeException("Alias parameter count (" + signatureParameterCount + ") does not match method argument count (" + getParameterCount() + "), method:" + method.getName());
        }
    }
    
}
