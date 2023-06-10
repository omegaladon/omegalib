package me.omega.omegalib.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A utility class for reflection.
 */
@UtilityClass
public class Reflection {

    /**
     * Gets a method from a class.
     *
     * @param clazz the class to get the method from
     * @param name  the name of the method
     * @param args  the arguments of the method
     * @return the method wrapped in a {@link MethodAccessor}
     * @see MethodAccessor
     */
    public static MethodAccessor getMethod(@NonNull Class<?> clazz, @NonNull String name, Class<?>... args) {
        try {
            Method method = clazz.getDeclaredMethod(name, args);
            method.setAccessible(true);
            return new MethodAccessor(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A wrapper for methods that allows for easy invocation.
     *
     * @param method the method to wrap
     */
    public record MethodAccessor(@NonNull Method method) {

        /**
         * Invokes the method on the specified instance with the specified arguments.
         *
         * @param instance the instance to invoke the method on
         * @param args     any arguments to pass to the method
         * @return the return value of the method, or null if an exception occurred
         * @throws InvocationTargetException if an exception occurs while invoking the method
         */
        public Object invoke(@NonNull Object instance, Object... args) throws InvocationTargetException {
            try {
                return method.invoke(instance, args);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
