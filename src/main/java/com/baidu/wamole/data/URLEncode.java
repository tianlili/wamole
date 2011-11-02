package com.baidu.wamole.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * field with this annotation should be encode
 * @author yangbo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface URLEncode {
	String enc() default "UTF-8";
}
