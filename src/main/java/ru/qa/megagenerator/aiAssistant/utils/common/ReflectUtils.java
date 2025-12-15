package ru.qa.megagenerator.aiAssistant.utils.common;

import com.intellij.openapi.application.ApplicationManager;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import static ru.qa.megagenerator.aiAssistant.constants.CommonConstants.reflections;

public class ReflectUtils {

    public static <A extends Annotation, T> List<T> getAnnotatedInstances(Class<A> annotationClass, Class<T> castTo, Reflections reflections) {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotationClass);
        return annotatedClasses.stream().map(aClass -> {
            try {
                return castTo.cast(aClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating " + aClass.getName(), e);
            }
        }).toList();
    }

    public static <T> List<T> getSubClassesServices(Class<T> baseClass) {
        Set<? extends Class<? extends T>> subTypes = reflections.getSubTypesOf(baseClass);
        return subTypes.stream().map(clazz -> {
                    T service = ApplicationManager.getApplication().getService(clazz);
                    if (service == null)
                        throw new IllegalStateException("Class %s not registered as Service."
                                .formatted(clazz.getName()));
                    return service;
                }).toList();
    }
}
