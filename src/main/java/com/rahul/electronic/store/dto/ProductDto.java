package com.rahul.electronic.store.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {

	
	
	private String productId;
	private String title;
	private String description;
	private int price;
	private int discountedPrice;
	private int quantatity;
	private Date addedDate;
	private boolean live;
	private boolean stock;
	
	
	
}
