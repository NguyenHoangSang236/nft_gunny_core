package com.nftgunny.core.utils;

import com.nftgunny.core.config.constant.ConstantValue;
import com.nftgunny.core.config.constant.SystemConfigCriteria;
import com.nftgunny.core.config.meta_data.SystemConfigKeyName;
import com.nftgunny.core.entities.database.SystemConfig;
import com.nftgunny.core.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitiateConfigUtils {
    final SystemConfigEnvUtils systemConfigEnvUtils;
    final SystemConfigRepository systemConfigRepo;

    public void initSslConfig() {
        // SSL configs
        String sslKeyStoreAlias = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_ALIAS);
        String sslKeyStoreType = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_TYPE);
        String sslKeyStorePassword = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_KEYSTORE_PASSWORD);
        String sslFileDirectory = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.SSL_FILE_DIRECTORY);

        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_ALIAS.name(), sslKeyStoreAlias);
        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_TYPE.name(), sslKeyStoreType);
        System.setProperty(SystemConfigKeyName.SSL_KEYSTORE_PASSWORD.name(), sslKeyStorePassword);
        System.setProperty(SystemConfigKeyName.SSL_FILE_DIRECTORY.name(), sslFileDirectory);


        // Server ports
        String eurekaServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.EUREKA_SERVER_PORT);
        String authenServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.AUTHENTICATION_SERVICE_SERVER_PORT);
        String resourceServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.PLAYERHUB_SERVICE_SERVER_PORT);
        String redisServerPort = systemConfigEnvUtils.getCredentials(SystemConfigKeyName.REDIS_SERVICE_SERVER_PORT);

        System.setProperty(SystemConfigKeyName.EUREKA_SERVER_PORT.name(), eurekaServerPort);
        System.setProperty(SystemConfigKeyName.AUTHENTICATION_SERVICE_SERVER_PORT.name(), authenServerPort);
        System.setProperty(SystemConfigKeyName.PLAYERHUB_SERVICE_SERVER_PORT.name(), resourceServerPort);
        System.setProperty(SystemConfigKeyName.REDIS_SERVICE_SERVER_PORT.name(), redisServerPort);

        log.info("Finished initiating SSL config");
    }

    public void getWeightSystemConfig() {
        List<SystemConfig> weightConfigs = systemConfigRepo.findByIds(
                List.of(
                        SystemConfigCriteria.ATTACK_WEIGHT.name(),
                        SystemConfigCriteria.DEFENSE_WEIGHT.name(),
                        SystemConfigCriteria.MAGIC_WEIGHT.name()
                )
        );

        StringBuilder stringBuilder = new StringBuilder();

        for(SystemConfig config : weightConfigs) {
            if(config.getId().equals(SystemConfigCriteria.ATTACK_WEIGHT.name())) {
                ConstantValue.SYSTEM_CONFIG.put(
                        SystemConfigCriteria.ATTACK_WEIGHT.name(),
                        config.getValue()
                );
                stringBuilder.append("Attack, ");
            }
            else if(config.getId().equals(SystemConfigCriteria.DEFENSE_WEIGHT.name())) {
                ConstantValue.SYSTEM_CONFIG.put(
                        SystemConfigCriteria.DEFENSE_WEIGHT.name(),
                        config.getValue()
                );
                stringBuilder.append("Defense, ");
            }
            else {
                ConstantValue.SYSTEM_CONFIG.put(
                        SystemConfigCriteria.MAGIC_WEIGHT.name(),
                        config.getValue()
                );
                stringBuilder.append("Magic, ");
            }

            if(stringBuilder.isEmpty()) {
                log.info("Failed to get all Weights configurations");
                            }
            else {
                log.info("Finished getting Weight configurations of {}", stringBuilder);
            }
        }
    }
}
