package com.nftgunny.core.entities.api.response;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseMapper {
    public static ResponseEntity<ApiResponse> map(ApiResponse response) {
        return new ResponseEntity<>(response, response.getHeaders(), response.getStatus());
    }
}
