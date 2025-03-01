package com.nftgunny.core.repository;

import com.nftgunny.core.entities.database.SystemConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemConfigRepository extends MongoRepository<SystemConfig, String> {
    @Query("{'name': ?0}")
    Optional<SystemConfig> findByName(String name);
}
