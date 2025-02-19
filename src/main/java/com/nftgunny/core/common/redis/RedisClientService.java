package com.nftgunny.core.common.redis;

import com.nftgunny.core.entities.database.DbEntity;
import com.nftgunny.core.entities.database.MongoDbEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RedisClientService {
    void cleanAndRecacheDataFromOneTable(Class<? extends DbEntity> dbEntityClass, String id, Map<String, Object> data, Boolean isSafe, List<Class<? extends CachingProcessHandler>> cachingProcessHandlers);
    void cleanAndRecacheDataListFromOneTable(Class<? extends DbEntity> dbEntityClass, String id, List<Map<String, Object>> dataList, Boolean isSafe, List<Class<? extends CachingProcessHandler>> cachingProcessHandlers);
    MongoDbEntity getAndCacheDataFromOneTable(Class<? extends DbEntity> entityClass, String id, List<Class<? extends CachingProcessHandler>> cachingProcessHandlers) throws Exception;
//    TableName getTableNameFromEntityClass(Class<? extends DbEntity> entityClass);
}
