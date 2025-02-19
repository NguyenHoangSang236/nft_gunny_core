package com.nftgunny.core.entities.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nftgunny.core.common.usecase.UseCase;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse implements UseCase.OutputValue, Serializable {
    private String result;
    private Object content;
    private String message;
    private String jwt;
    private String refreshToken;
    private HttpStatus status;
    private List<String> errors;
    @JsonIgnore
    private HttpHeaders headers;

    public ApiResponse(String result, Object content, HttpStatus status) {
        this.result = result;
        this.content = content;
        this.status = status;
    }

    public ApiResponse(String result, Object content, String message) {
        this.result = result;
        this.content = content;
        this.message = message;
    }

    public ApiResponse(String result, Object content) {
        this.result = result;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "result='" + result + '\'' +
                ", content=" + content +
                ", message='" + message + '\'' +
                ", jwt='" + jwt + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", status='" + status.toString() + '\'' +
                ", errors='" + errors + '\'' +
                '}';
    }
}
