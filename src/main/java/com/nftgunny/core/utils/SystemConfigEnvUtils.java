package com.nftgunny.core.utils;


import com.nftgunny.core.config.meta_data.SystemConfigKeyName;
import com.nftgunny.core.config.meta_data.SystemMetaData;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigEnvUtils {
    public String getCredentials(SystemConfigKeyName key) {
        Dotenv dotenv = Dotenv
                .configure()
                .filename(SystemMetaData.CREDENTIALS_FILE_PATH)
                .load();

        return dotenv.get(key.name());
    }
}
