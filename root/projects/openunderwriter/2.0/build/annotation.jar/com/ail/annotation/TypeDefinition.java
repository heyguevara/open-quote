package com.ail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface TypeDefinition {
    static final String DEFAULT_NAME="TypeDefinitionDefaultName";

    String name() default DEFAULT_NAME;
}
