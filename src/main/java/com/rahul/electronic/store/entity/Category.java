package com.rahul.electronic.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "categories")
public class Category {
	@Id
	@Column(name = "id")
	private String categoryId;
	@Column(name = "category_title",length = 60,nullable = false)
	private String title;
	@Column(name="category_desc",length = 60)
	private String description;
	private String coverImage;

}
