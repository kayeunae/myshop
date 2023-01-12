package com.myshop.dto;

import java.time.LocalDateTime;
import com.myshop.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
	
	private Long id;	//상품코드
	
	private String itemNm;	//상품명
	
	private int price;	//가격

	private int stockNumber;	//재고수량
	
	private String itemDetail;	//상품 상세설명
	
	private ItemSellStatus itemSellStatus;	//상품 판매상태

	private LocalDateTime regTime;	//등록 시간
	
	private LocalDateTime updateTime;	//수정 시간
}
