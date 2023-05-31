package org.example.core;

import java.lang.reflect.InvocationTargetException;

public interface UniqueKeyCache<T> {

    String NAME_PREFIX = "gw_";
    String READ_ONLY_SUFFIX = "_read";

    Long getId();

    void setId(Long id);

    String getUniqueKey();

    void setUniqueKey(String uniqueKey);

    default String name() {
        return NAME_PREFIX + this.getClass().getSimpleName();
    }

    default String readOnlyName() {
        return name();
    }

    default boolean syncWithDatabase() {
        return false;
    }

    default T of()
            throws Exception
    {
        try {
            return (T) this.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new Exception("Failed to build instance");
    }
}
