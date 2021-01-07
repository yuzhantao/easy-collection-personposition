package com.bimuo.easy.collection.personposition.core.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseStatus {
	/**
	 * 状态编码
	 * @return
	 */
	int value() default HttpStatus.INTERNAL_SERVER_ERROR;
	/**
	 * 返回的状态信息
	 * @return
	 */
	String reason() default "";
}
