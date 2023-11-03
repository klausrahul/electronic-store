package com.rahul.electronic.store.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.electronic.store.dto.ApiResponseMessage;
import com.rahul.electronic.store.dto.CreateOrderRequest;
import com.rahul.electronic.store.dto.OrderDto;
import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
		
		OrderDto order=orderService.createOrder(request);
		return new ResponseEntity<OrderDto>(order,HttpStatus.CREATED);
		
	}
	

	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
		
		orderService.removeOrder(orderId);
		ApiResponseMessage response= ApiResponseMessage.builder().message("Order Removed").success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(response,HttpStatus.OK);
		
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<List<OrderDto>> getorderByUser(@PathVariable String userId){
		
		List<OrderDto> order=orderService.getOrderOfuser(userId);
		return new ResponseEntity<List<OrderDto>>(order,HttpStatus.OK);
		
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,

			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,

			@RequestParam(value = "sortBy", required = false, defaultValue = "orderedDate") String sortBy,

			@RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir){
		
		PageableResponse<OrderDto> order=orderService.getOrders(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PageableResponse<OrderDto>>(order,HttpStatus.OK);
		
	}
	
}
