package com.radovan.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.radovan.spring.exceptions.ExistingInstanceException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.exceptions.SuspendedUserException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(ExistingInstanceException.class)
	public ResponseEntity<String> handleExistingInstanceException(Error error) {
		return new ResponseEntity<String>(error.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(SuspendedUserException.class)
	public ResponseEntity<String> handleSuspendedUserException(Error error) {
		SecurityContextHolder.clearContext();
		return new ResponseEntity<String>(error.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
	}

	@ExceptionHandler(InstanceUndefinedException.class)
	public ResponseEntity<String> handleInstanceUndefinedException(Error error) {
		return new ResponseEntity<String>(error.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
