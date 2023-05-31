package org.example.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.example.model.TimeBasedModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

public interface TimeBasedRepository<T extends TimeBasedModel<T>, ID>
        extends CrudRepository<T, ID>
{
    T findByUniqueKey(String uniqueKey);

    @Modifying
    void deleteByUniqueKey(String uniqueKey);

    default <R extends T> List<R> findExpireRecords() {
        return findByUpdateTimeBefore(System.currentTimeMillis() - 2 * 60 * 1000);
    }

    <R extends T> List<R> findByUpdateTimeBefore(Long time);

    default boolean canForceExpire() {
        return true;
    }

    default <S extends T> S saveOrUpdate(S var1) {
        if (var1.getCreateTime() == null || var1.getCreateTime() == 0) {
            var1.setCreateTime(System.currentTimeMillis());
        }
        var1.setUpdateTime(System.currentTimeMillis());
        return save(var1);
    }

    default String getActualSimpleName() {
        ParameterizedType genericInterface = (ParameterizedType) ((Class) this.getClass()
                // interface
                .getGenericInterfaces()[0])
                //base class
                .getGenericInterfaces()[0];
        Type[] actualTypeArguments = genericInterface.getActualTypeArguments();
//        return NAME_PREFIX + ((Class<T>) actualTypeArguments[0]).getSimpleName();
        return "gw_" + ((Class<T>) actualTypeArguments[0]).getSimpleName();
    }
}
