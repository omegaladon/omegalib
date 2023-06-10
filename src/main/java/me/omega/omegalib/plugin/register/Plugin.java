package me.omega.omegalib.plugin.register;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * An annotation used to automatically generate a plugin.yml file.
 */
@Target(ElementType.TYPE)
public @interface Plugin {
    String name();

    String version();

    String main();

    String description() default "";

    String[] authors() default {};

    String website() default "";

    ApiVersion apiVersion();

    Load load() default Load.POSTWORLD;

    String prefix() default "";

    String[] depend() default {};

    String[] softDepend() default {};

    String[] loadBefore() default {};
}
