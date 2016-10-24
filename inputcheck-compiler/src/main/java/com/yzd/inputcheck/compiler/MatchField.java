package com.yzd.inputcheck.compiler;

import com.yzd.inputcheck.annotation.Match;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * 封装Match注解的对象
 *
 * Created by yanzhaodi on 2016/10/21.
 */
public class MatchField {

    // 错误信息
    private int messageId;

    // 要匹配的正则
    private String regex;

    // 被Match注解的变量
    private VariableElement mField;

    public MatchField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", Match.class.getSimpleName()));
        }

        if (element.getModifiers().contains(Modifier.PRIVATE) || element.getModifiers().contains(Modifier.STATIC)) {
            throw new IllegalArgumentException(
                    String.format("@%s can't used on private or static", Match.class.getSimpleName()));
        }

        mField = (VariableElement) element;

        Match match = mField.getAnnotation(Match.class);
        messageId = match.message();
        regex = match.regex();
    }

    public int getMessage() {
        return messageId;
    }

    public String getRegex() {
        return regex;
    }

    public String getFieldName() {
        return mField.getSimpleName().toString();
    }
}
