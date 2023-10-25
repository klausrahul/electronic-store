package com.rahul.electronic.store.entity;

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
@Entity
@Table(name="products")
public class Product {

	
	
	private String productId;
	private String title;
	@Column(length = 100000)
	private String description;
	private int price;
	private int discountedPrice;
	private int quantatity;
	private Date addedDate;
	private boolean live;
	private boolean stock;
	
	
	
}
