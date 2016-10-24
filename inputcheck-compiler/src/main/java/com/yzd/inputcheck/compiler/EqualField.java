package com.yzd.inputcheck.compiler;

import com.yzd.inputcheck.annotation.Equal;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Created by yanzhaodi on 2016/10/22.
 */

public class EqualField {

    private VariableElement mField;

    private int messageId;

    private String target;

    public EqualField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", Equal.class.getSimpleName()));
        }

        if (element.getModifiers().contains(Modifier.PRIVATE) || element.getModifiers().contains(Modifier.STATIC)) {
            throw new IllegalArgumentException(
                    String.format("@%s can't used on private or static", Equal.class.getSimpleName()));
        }

        mField = (VariableElement) element;

        Equal equal = mField.getAnnotation(Equal.class);

        messageId = equal.message();
        target = equal.target();
    }

    public int getMessage() {
        return messageId;
    }

    public String getTarget() {
        return target;
    }

    public String getFieldName() {
        return mField.getSimpleName().toString();
    }
}
