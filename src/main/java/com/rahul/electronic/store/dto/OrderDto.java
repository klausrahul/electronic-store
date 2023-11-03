package com.rahul.electronic.store.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
	private String orderId;
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	private int orderAmount;
	private String billingAddress;
	private String billingPhone;
	private String billingName;
	private Date orderDate=new Date();
	
	private Date deliveredDate;
	
	//private User user;
	
	private List<OrderItemDto> orderItems=new ArrayList<>();
}
