package com.rahul.electronic.store.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rahul.electronic.store.dto.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
		log.info("Exception Handler invoked !!");
		ApiResponseMessage responseMessage=ApiResponseMessage.builder().message(ex.getMessage()).success(true).status(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<ApiResponseMessage>(responseMessage,HttpStatus.NOT_FOUND);
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handelMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		log.info("handelMethodArgumentNotValidException Handler invoked !!");
		List<ObjectError> allError=ex.getBindingResult().getAllErrors();
		Map<String, Object> response=new HashMap<>();
		allError.stream().forEach(obj -> {
			String message=obj.getDefaultMessage();
			String field=((FieldError) obj).getField();
			response.put(field, message);
		});
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponseMessage> handleBadAPIRequest(BadApiRequestException ex){
		log.info("handleBadAPIRequest Handler invoked !!");
		ApiResponseMessage responseMessage=ApiResponseMessage.builder().message(ex.getMessage()).success(false).status(HttpStatus.BAD_REQUEST).build();
		return new ResponseEntity<ApiResponseMessage>(responseMessage,HttpStatus.BAD_REQUEST);
	}
	
	
}
