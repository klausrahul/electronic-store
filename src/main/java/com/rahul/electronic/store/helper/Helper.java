package com.rahul.electronic.store.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.serviceImpl.CategoryServiceImpl;

public class Helper {
	private static Logger log = LoggerFactory.getLogger(Helper.class);
	public static <U,V> PageableResponse<V> getpageableResponse(Page<U> page, Class<V> type){
		log.info("inside helpeer");
		List<U> entity = page.getContent();

		List<V> alluserDto = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
		
		PageableResponse<V> response=new PageableResponse<>();
		response.setContent(alluserDto);
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setLastPage(page.isLast());
		
		return response;
	}

}
