package com.baidu.wamole.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * for export 
 * 
 * @author yangbo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Exported{
	boolean recurse() default true;
	boolean encode() default false;
	String enc() default "UTF-8";
}
