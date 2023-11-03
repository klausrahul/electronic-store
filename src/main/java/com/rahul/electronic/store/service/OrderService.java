package com.rahul.electronic.store.service;

import java.util.List;

import com.rahul.electronic.store.dto.CreateOrderRequest;
import com.rahul.electronic.store.dto.OrderDto;
import com.rahul.electronic.store.dto.PageableResponse;

public interface OrderService {

	OrderDto createOrder(CreateOrderRequest orderDto);
	
	void removeOrder(String orderId);
	
	List<OrderDto> getOrderOfuser(String userId);
	PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
}
