package com.mwozniak.capser_v2.achievements;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SinglesAchievement
@EasyAchievement
@DoublesAchievement
public @interface GeneralAchievement {

    String value() default "";

}
