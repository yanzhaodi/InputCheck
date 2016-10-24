package com.yzd.inputcheck;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanzhaodi on 2016/10/20.
 */

public class InputCheck {

    private static final Map<String, Check> CHECK_MAP = new HashMap<>();

    public static String check(Object target) {
        String className = target.getClass().getName();

        try {
            Check check = CHECK_MAP.get(className);
            if (check == null) {
                Class<?> finderClass = Class.forName(className + "$$Check");
                check = (Check) finderClass.newInstance();
                CHECK_MAP.put(className, check);
            }
            return check.check(target);
        } catch (Exception e) {
            throw new RuntimeException("Unable to check for " + className, e);
        }
    }
}
