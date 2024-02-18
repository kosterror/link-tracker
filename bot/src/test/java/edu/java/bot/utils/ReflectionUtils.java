package edu.java.bot.utils;

import java.lang.reflect.Field;

/**
 * Вспомогательный класс, использующий рефлексию для получения доступа к приватным полям.
 */
public class ReflectionUtils {

    /**
     * Возвращает значение строковое значение поля из класса.
     *
     * @param clazz     класс, содержащий поле.
     * @param fieldName имя поля.
     * @return значение поля.
     */
    public static String getPSFString(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            return (String) field.get(null);
        } catch (Exception exception) {
            throw new RuntimeException("Не получилось получить значение", exception);
        }

    }
}
