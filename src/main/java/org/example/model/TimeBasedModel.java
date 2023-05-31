package org.example.model;

import org.example.core.UniqueKeyCache;

import java.io.Serializable;

public interface TimeBasedModel<T> extends UniqueKeyCache<T>, Serializable {

    Long getUpdateTime();

    void setUpdateTime(Long updateTime);

    Long getCreateTime();

    void setCreateTime(Long createTime);

    default boolean updateRemoteId() {
        return true;
    }
}
