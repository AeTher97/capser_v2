package com.mwozniak.capser_v2.achievements;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DoublesAchievement {

    String value() default "";
}
