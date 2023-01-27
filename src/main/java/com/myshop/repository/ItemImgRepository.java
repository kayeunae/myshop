package com.myshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
	
	List<ItemImg> findByItemIdOrderByIdAsc(Long ItemId);
	
	//상품의 대표 이미지를 찾음
	ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
