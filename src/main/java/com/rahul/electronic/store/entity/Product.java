package com.rahul.electronic.store.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

	
	@Id
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
	
	private String productImageName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	//@JoinColumn(name = "category")
	private Category category;

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", title=" + title + ", description=" + description + ", price="
				+ price + ", discountedPrice=" + discountedPrice + ", quantatity=" + quantatity + ", addedDate="
				+ addedDate + ", live=" + live + ", stock=" + stock + ", productImageName=" + productImageName
				+ ", category=" + category + "]";
	}
	
	
	
}
