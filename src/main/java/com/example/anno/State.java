package com.example.anno;

import com.example.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;

@Documented//元註解
@Target({FIELD})//元註解
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {StateValidation.class})//指定提供校驗規則的類

public @interface State {
    //提供校驗失敗的提示信息
    String message() default "state的參數只能是'已發布'或著'草稿'";
    //指定分組
    Class<?>[] groups() default {};
    //  獲取到State註解的附加信息
    Class<? extends Payload>[] payload() default {};
}
