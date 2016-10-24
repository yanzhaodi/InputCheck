package com.yzd.inputcheck;

/**
 * Created by yanzhaodi on 2016/10/20.
 */
public interface Check<T> {
    String check(T host);
}
