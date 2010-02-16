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

import fitlibrary.table.Cell;
import fitlibrary.table.Row;

import java.util.List;

public class SignatureMatcher {
    private List<String> signature;

    public SignatureMatcher(List<String> signature) {
        this.signature = signature;
    }

    public boolean matches(Row row) {
        if(row.size() != signature.size()) return false;
        for(int i = 0; i < row.size(); ++i) {
            String signatureToken = signature.get(i);
            String part = row.cell(i).text();
            if(!wildToken(signatureToken) && !part.equals(signatureToken)) {
                return false;
            }
        }
        return true;
    }

    private boolean wildToken(String signatureToken) {
        return "_".equals(signatureToken);
    }

}
