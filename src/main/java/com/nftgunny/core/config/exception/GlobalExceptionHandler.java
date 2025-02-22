package com.nftgunny.core.config.exception;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.nftgunny.core.config.constant.ConstantValue;
import com.nftgunny.core.config.constant.ExceptionType;
import com.nftgunny.core.config.constant.ResponseResult;
import com.nftgunny.core.entities.api.response.ApiResponse;
import com.nftgunny.core.entities.exceptions.DataNotFoundException;
import com.nftgunny.core.entities.exceptions.IdNotFoundException;
import com.nftgunny.core.entities.exceptions.InvalidFileExtensionException;
import com.mongodb.MongoWriteException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(ApiResponse.builder()
                .result(ResponseResult.failed.name())
                .errors(Collections.singletonList(ConstantValue.UNAUTHORIZED_ERROR_MESSAGE))
                .message(ExceptionType.AUTHENTICATION.getValue())
                .status(HttpStatus.UNAUTHORIZED)
                .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList(ConstantValue.UNAUTHORIZED_ERROR_MESSAGE))
                        .message(ExceptionType.AUTHENTICATION.getValue())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList(ConstantValue.ACCESS_ERROR_MESSAGE))
                        .message(ExceptionType.ACCESS.getValue())
                        .status(HttpStatus.FORBIDDEN)
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = {DataNotFoundException.class})
    protected ResponseEntity<ApiResponse> handleDataNotFoundException(DataNotFoundException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList(ex.getMessage()))
                        .message(ExceptionType.DATA_NOT_FOUND.getValue())
                        .status(HttpStatus.NO_CONTENT)
                        .build(),
                HttpStatus.NO_CONTENT
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {IdNotFoundException.class})
    protected ResponseEntity<ApiResponse> handleIdNotFoundException(IdNotFoundException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList(ex.getMessage()))
                        .message(ExceptionType.DATA_NOT_FOUND.getValue())
                        .status(HttpStatus.NOT_FOUND)
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DuplicateKeyException.class})
    protected ResponseEntity<ApiResponse> handleMongoWriteException(DuplicateKeyException ex) {
        ex.printStackTrace();

        if (ex.getCause() instanceof MongoWriteException mongoWriteEx) {

            int code = mongoWriteEx.getCode();

            if (code == 11000) {
                String jsonStr = mongoWriteEx.getError().getMessage().substring(mongoWriteEx.getError().getMessage().indexOf("{"));

                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .result(ResponseResult.failed.name())
                                .errors(Collections.singletonList("Duplicated data: " + jsonStr))
                                .message(ExceptionType.DATA_DUPLICATED.getValue())
                                .status(HttpStatus.BAD_REQUEST)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            } else {
                return new ResponseEntity<>(
                        ApiResponse.builder()
                                .result(ResponseResult.failed.name())
                                .errors(Collections.singletonList(mongoWriteEx.getMessage()))
                                .message(ExceptionType.DATA_DUPLICATED.getValue())
                                .status(HttpStatus.BAD_REQUEST)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );
            }

        } else {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .result(ResponseResult.failed.name())
                            .errors(Collections.singletonList(ex.getMessage()))
                            .message(ExceptionType.DATA_DUPLICATED.getValue())
                            .status(HttpStatus.BAD_REQUEST)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    String builder = "Field " +
                            getFieldName(constraintViolation.getPropertyPath().toString()) +
                            " " +
                            constraintViolation.getMessage();

                    return builder;
                }).toList();
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(errors)
                        .message(ExceptionType.VALIDATOR.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {ExpiredJwtException.class})
    protected ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList(ex.getMessage()))
                        .message(ExceptionType.JWT_EXPIRED.getValue())
                        .status(HttpStatus.UNAUTHORIZED)
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {InvalidFormatException.class})
    protected ResponseEntity<ApiResponse> handleInvalidFormatException(InvalidFormatException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message(ExceptionType.INVALID_FORMAT.getValue())
                        .errors(Collections.singletonList("JSON parse error, there must be some variable does not exist or invalid, please check again"))
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();
        
        List<String> errors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessageForField = error.getDefaultMessage();
                errors.add(String.format("Field '%s': %s; ", fieldName, errorMessageForField));
            }
        });

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(errors)
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(errors)
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                headers,
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList(ConstantValue.INTERNAL_SERVER_ERROR_MESSAGE))
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleInvalidFileExtensionException(InvalidFileExtensionException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message(ExceptionType.FILE.getValue())
                        .errors(Collections.singletonList(ConstantValue.INTERNAL_SERVER_ERROR_MESSAGE))
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleJacksonException(JacksonException ex) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message(ExceptionType.INVALID_FORMAT.getValue())
                        .errors(Collections.singletonList(ConstantValue.INTERNAL_SERVER_ERROR_MESSAGE))
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleEnumConversionError(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String message = String.format(
                    "Invalid value '%s' for field '%s'. Expected one of: %s",
                    ex.getValue(),
                    ex.getName(),
                    Arrays.toString(ex.getRequiredType().getEnumConstants())
            );
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .result(ResponseResult.failed.name())
                            .errors(Collections.singletonList(message))
                            .message(ExceptionType.INVALID_PARAMETER.getValue())
                            .status(HttpStatus.BAD_REQUEST)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Handle other cases
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList("Invalid input"))
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }


    @Override
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .message(ExceptionType.INVALID_PARAMETER.getValue())
                        .errors(Collections.singletonList("Field " + ex.getParameterName() + " must not be null"))
                        .status(HttpStatus.BAD_REQUEST)
                        .build(),
                HttpStatus.BAD_REQUEST
        );    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ex.printStackTrace();

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .errors(Collections.singletonList(ConstantValue.INTERNAL_SERVER_ERROR_MESSAGE))
                        .message(ExceptionType.INTERNAL.getValue())
                        .status(HttpStatus.valueOf(status.value()))
                        .build(),
                headers,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    private String getFieldName(String propertyPath) {
        return propertyPath.substring(propertyPath.indexOf(".") + 1);
    }
}
