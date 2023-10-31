package com.rahul.electronic.store.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.rahul.electronic.store.entity.Category;

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
	
	private String productImageName;
	private CategoryDto category;
	@Override
	public String toString() {
		return "ProductDto [productId=" + productId + ", title=" + title + ", description=" + description + ", price="
				+ price + ", discountedPrice=" + discountedPrice + ", quantatity=" + quantatity + ", addedDate="
				+ addedDate + ", live=" + live + ", stock=" + stock + ", productImageName=" + productImageName
				+ ", categoryDto=" + category + "]";
	}
	
	
	
}
