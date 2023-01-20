package com.myshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myshop.dto.ItemSearchDto;
import com.myshop.entity.Item;

//1. 사용자 정의 인터페이스
public interface ItemRepositoryCustom {
	//검색한 데이터를 받아오는데 페이징 처리를 해서 받아온다.
	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}




