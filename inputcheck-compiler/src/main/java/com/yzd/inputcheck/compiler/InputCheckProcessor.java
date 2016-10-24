package com.yzd.inputcheck.compiler;

import com.google.auto.service.AutoService;
import com.yzd.inputcheck.annotation.Equal;
import com.yzd.inputcheck.annotation.Match;
import com.yzd.inputcheck.annotation.NotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by yanzhaodi on 2016/10/21.
 */
@AutoService(Processor.class)
public class InputCheckProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager messager;

    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(NotEmpty.class.getCanonicalName());
        annotations.add(Match.class.getCanonicalName());
        annotations.add(Equal.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(NotEmpty.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            NotEmptyField field = new NotEmptyField(element);
            annotatedClass.addNotEmptyField(field);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Match.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            MatchField field = new MatchField(element);
            annotatedClass.addMatchField(field);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Equal.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            EqualField field = new EqualField(element);
            annotatedClass.addEqualField(field);
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                annotatedClass.generateCheck().writeTo(mFiler);
            } catch (IOException e) {
                return true;
            }
        }
        return true;
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        String fullClassName = classElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(classElement, mElementUtils);
            mAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }
}
