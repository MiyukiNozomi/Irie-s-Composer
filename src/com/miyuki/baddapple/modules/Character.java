package com.miyuki.baddapple.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Module Information Annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Character {
	/**Your module's names, its a required field.*/
	String name();
	/**Your module's description, its a required field.*/
	String description();
	/**Path to your module's icon, by default it uses BadApple's icon.*/
	String iconPath() default "internal://icon.png";
	/**Version of your module, by default 0.*/
	String version() default "0";
}
