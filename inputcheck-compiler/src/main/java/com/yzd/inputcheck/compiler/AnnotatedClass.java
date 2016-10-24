package com.yzd.inputcheck.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 带inputcheck的注解的类
 *
 * Created by yanzhaodi on 2016/10/21.
 */
public class AnnotatedClass {

    private TypeElement mClassElement;

    private List<NotEmptyField> mNotEmptyFields;
    private List<MatchField> mMatchFields;
    private List<EqualField> mEqualFields;

    private Elements mElementUtils;

    public AnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;

        mNotEmptyFields = new ArrayList<>();
        mMatchFields = new ArrayList<>();
        mEqualFields = new ArrayList<>();
    }

    public void addNotEmptyField(NotEmptyField field) {
        mNotEmptyFields.add(field);
    }

    public void addMatchField(MatchField field) {
        mMatchFields.add(field);
    }

    public void addEqualField(EqualField field) {
        mEqualFields.add(field);
    }

    /**
     * 生成Check代码
     */
    public JavaFile generateCheck() {
        MethodSpec.Builder checkBuilder = MethodSpec.methodBuilder("check")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .returns(String.class);

        for (NotEmptyField field : mNotEmptyFields) {
            checkBuilder.beginControlFlow("if (android.text.TextUtils.isEmpty(host.$L.getText().toString()))", field.getFieldName());

            checkBuilder.addStatement("return host.getString($L)", field.getMessage());
            checkBuilder.endControlFlow();
        }

        for (MatchField field : mMatchFields) {
            checkBuilder.beginControlFlow("if (!host.$L.getText().toString().matches($S))", field.getFieldName(), field.getRegex());

            checkBuilder.addStatement("return host.getString($L)", field.getMessage());
            checkBuilder.endControlFlow();
        }

        for (EqualField field : mEqualFields) {
            checkBuilder.beginControlFlow("if (!host.$L.getText().toString().equals(host.$L.getText().toString()))", field.getFieldName(), field.getTarget());

            checkBuilder.addStatement("return host.getString($L)", field.getMessage());
            checkBuilder.endControlFlow();
        }

        checkBuilder.addStatement("return null");

        TypeSpec checkClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Check")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.CHECK, TypeName.get(mClassElement.asType())))
                .addMethod(checkBuilder.build())
                .build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, checkClass).build();
    }

}
