package com.ail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface CommandDefinition {
	@SuppressWarnings("rawtypes")
	Class defaultServiceClass() default DEFAULT_SERVICE_CLASS.class;
	static final class DEFAULT_SERVICE_CLASS {};
}
