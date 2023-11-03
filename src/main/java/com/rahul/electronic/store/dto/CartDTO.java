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
public class CartDTO {
	private String cartId;
	private Date createdAT;
	private UserDto user;
	private List<CartItemDto> items=new ArrayList<>();
}
