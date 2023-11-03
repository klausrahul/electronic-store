package com.rahul.electronic.store.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
	
	@NotBlank(message = "Cart Id requried!!")
	private String cartId;
	@NotBlank(message = "User Id requried!!")
	private String userId;
	
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	@NotBlank(message = "billingAddress  requried!!")
	private String billingAddress;
	@NotBlank(message = "billingPhone  requried!!")
	private String billingPhone;
	@NotBlank(message = "billingName  requried!!")
	private String billingName;
	
	
	
}
