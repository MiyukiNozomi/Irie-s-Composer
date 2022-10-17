package com.miyuki.baddapple.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Character {
	String name();
	String description();
	String iconPath() default "internal://icon.png";
	String version() default "0";
}
