package com.pradeep.smart_parking_api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SlotNotAvailableException.class)
	public ResponseEntity<Map<String, Object>> handleSlotNotAvailable(SlotNotAvailableException e) {
		return buildResponse(HttpStatus.CONFLICT, e.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException e) {
		return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException e) {
		return buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicateEmail(DuplicateEmailException e) {
		return buildResponse(HttpStatus.CONFLICT, e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
		String message = e.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(Collectors.joining(", "));
		return buildResponse(HttpStatus.BAD_REQUEST, message);
	}

	// Two users tried to change the same slot at the same moment
	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<Map<String, Object>> handleOptimisticLock(ObjectOptimisticLockingFailureException e) {
		return buildResponse(HttpStatus.CONFLICT,
				"This slot was just taken by someone else. Please try another slot.");
	}

	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}
}