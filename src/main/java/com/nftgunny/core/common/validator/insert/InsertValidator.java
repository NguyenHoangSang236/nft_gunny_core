package com.nftgunny.core.common.validator.insert;

import com.nftgunny.core.utils.CheckUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InsertValidator implements ConstraintValidator<InsertValid, Object> {
    final CheckUtils checkUtils;
    String message;
    String phoneMessage;
    String emailMessage;
    String sha256Message;
    String nullMessage;
    boolean isPhoneNumber;
    boolean isSha256;
    boolean isEmail;
    boolean nullable;

    @Override
    public void initialize(InsertValid constraintAnnotation) {
        this.emailMessage = constraintAnnotation.emailMessage();
        this.nullMessage = constraintAnnotation.nullMessage();
        this.sha256Message = constraintAnnotation.sha256Message();
        this.message = constraintAnnotation.message();
        this.phoneMessage = constraintAnnotation.phoneMessage();
        this.isPhoneNumber = constraintAnnotation.isPhoneNumber();
        this.isEmail = constraintAnnotation.isEmail();
        this.isSha256 = constraintAnnotation.isSha256();
        this.nullable = constraintAnnotation.nullable();

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!this.nullable && (value == null || value.toString().isBlank())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.nullMessage).addConstraintViolation();

            return false;
        }
        else {
            if(value == null || value.toString().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.nullMessage).addConstraintViolation();

                return false;
            }

            if (isEmail && !checkUtils.isValidEmail(value.toString())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.emailMessage).addConstraintViolation();

                return false;
            }

            if (isPhoneNumber && !checkUtils.isValidPhoneNumber(value.toString())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.phoneMessage).addConstraintViolation();

                return false;
            }

            if (isSha256 && !checkUtils.isSha256(value.toString())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.sha256Message).addConstraintViolation();

                return false;
            }

            return true;
        }
    }
}
