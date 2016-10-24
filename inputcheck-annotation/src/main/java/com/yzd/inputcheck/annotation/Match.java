package com.yzd.inputcheck.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Match {

    /**
     * 要匹配的正则表达式
     * @return  正则表达式
     */
    String regex();

    /**
     * 不匹配时，返回的错误信息
     * @return  错误信息
     */
    int message();

}
