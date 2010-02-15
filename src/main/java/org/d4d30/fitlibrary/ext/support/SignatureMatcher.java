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
