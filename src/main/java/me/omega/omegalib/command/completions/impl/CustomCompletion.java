package me.omega.omegalib.command.completions.impl;

import me.omega.omegalib.command.Command;
import me.omega.omegalib.command.completions.TabCompletion;

import java.lang.reflect.Method;
import java.util.List;

public class CustomCompletion extends TabCompletion {

    public CustomCompletion(String type, String value, Command instance) {
        super(type, value, instance);
    }

    @Override
    public List<String> get() {
        Method method;
        try {
            method = getInstance().getClass().getMethod(getValue());
            method.setAccessible(true);
            if (!method.getReturnType().equals(List.class)) {
                throw new IllegalArgumentException("The method " + getValue() + " does not return a List<String>.");
            }
            if (method.getParameterCount() != 0) {
                throw new IllegalArgumentException("The method " + getValue() + " must not have any parameters.");
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The method " + getValue() + " does not exist.");
        }

        Object result;
        try {
            result = method.invoke(getInstance());
        } catch (Exception e) {
            throw new IllegalStateException("The method " + getValue() + " could not be invoked.", e);
        }
        return (List<String>) result;
    }

}
