package com.yzd.inputcheck.compiler;

import com.yzd.inputcheck.annotation.NotEmpty;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * 封装NotEmpty注解的对象
 *
 * Created by yanzhaodi on 2016/10/21.
 */
public class NotEmptyField {

    // 错误信息
    private int messageId;

    // 被notempty注解的变量
    private VariableElement mField;

    public NotEmptyField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", NotEmpty.class.getSimpleName()));
        }

        if (element.getModifiers().contains(Modifier.PRIVATE) || element.getModifiers().contains(Modifier.STATIC)) {
            throw new IllegalArgumentException(
                    String.format("@%s can't used on private or static", NotEmpty.class.getSimpleName()));
        }

        mField = (VariableElement) element;

        NotEmpty notEmpty = mField.getAnnotation(NotEmpty.class);
        messageId = notEmpty.value();
    }

    public int getMessage() {
        return messageId;
    }

    public String getFieldName() {
        return mField.getSimpleName().toString();
    }
}
