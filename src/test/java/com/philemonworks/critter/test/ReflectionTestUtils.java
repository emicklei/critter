package com.philemonworks.critter.test;

import java.lang.reflect.Field;

/**
 * Insert documentation here.
 *
 * @author jcraane
 */
public class ReflectionTestUtils {
    public static void setField(final Object target, final String name, final Object type) {
        try {
            final Field declaredField = target.getClass().getDeclaredField("name");
            declaredField.setAccessible(true);
            declaredField.set(target, type);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
