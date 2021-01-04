package com.winbaoxian.testng.utils;

import java.util.List;

public enum WinTestNGCommonUtils {

    INSTANCE;

    public int getCollectionSize(List coll) {
        if (coll == null || coll.isEmpty()) {
            return 0;
        }
        return coll.size();
    }

}
