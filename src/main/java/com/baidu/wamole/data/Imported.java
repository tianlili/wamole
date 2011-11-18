package com.baidu.wamole.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for import data from io
 * @author yangbo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Imported {
	boolean decode() default false;
	boolean recurse() default false;
	String enc() default "UTF-8";
}
