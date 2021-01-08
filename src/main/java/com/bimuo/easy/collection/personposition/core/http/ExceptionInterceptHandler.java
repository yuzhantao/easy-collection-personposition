package com.bimuo.easy.collection.personposition.core.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 异常拦截处理
 * 
 * @author yuzhantao
 *
 */
@ControllerAdvice
public class ExceptionInterceptHandler {
	@ExceptionHandler({ Throwable.class })
	public ResponseEntity<?> exceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		ResponseEntity<?> responseEntity = null;
		if(ex instanceof BusinessException && ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
			ResponseStatus rs = ex.getClass().getAnnotation(ResponseStatus.class);
			responseEntity = ResponseEntity.status(rs.value()).body(rs.reason());
		}else {
			responseEntity = new ResponseEntity<>(ex.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
}
