package com.nftgunny.core.entities.api.request;

import java.util.Map;

public abstract class ApiRequest {
    public abstract Map<String, Object> toMap();
}
