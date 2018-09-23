package de.exb.platform.cloud.fileservice.client.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.exb.platform.cloud.fileservice.client.service.FileServiceException;

/**
 * Catches exceptions and provide a suitable response.
 * As a future improvement, it would be required to adjust the exception types thrown by the Controller,
 * and handle them all here (for instance, IOException)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@ExceptionHandler(FileServiceException.class)
	public ResponseEntity handleFileServiceException(Exception e) {
		logger.error("{}", e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	/**
	 * Same implementation as "handleFileServiceException", just to show that it could be extended to other
	 * exceptions as well (even though we could have a handler for generic exceptions too)
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(Exception e) {
		logger.error("{}", e.getMessage(), e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
