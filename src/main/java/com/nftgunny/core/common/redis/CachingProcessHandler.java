package com.nftgunny.core.common.redis;

import com.nftgunny.core.entities.database.MongoDbEntity;
import com.nftgunny.core.entities.exceptions.DataNotFoundException;
import com.nftgunny.core.entities.exceptions.InvalidDataException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CachingProcessHandler {
    private MongoDbEntity dbEntity;
    private Context context;

    public CachingProcessHandler(MongoDbEntity dbEntity) {
        this.dbEntity = dbEntity;
    }

    public CachingProcessHandler(Context context) {
        this.context = context;
    }

    /**
     * Execute some logic for context within caching process
     *
     */
    public abstract void process();
}
