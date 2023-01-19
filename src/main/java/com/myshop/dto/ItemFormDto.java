package com.myshop.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.*;

import org.modelmapper.ModelMapper;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;
import com.myshop.entity.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemFormDto {
	private Long id;	//상품코드
	
	@NotBlank(message = "상품명은 필수 입력 값입니다.")
	private String itemNm;	//상품명

	@NotNull(message = "가격은 필수 입력 값입니다.")
	private int price;	//가격

	@NotNull(message = "재고는 필수 입력 값입니다.")
	private int stockNumber;	//재고수량
	
	@NotBlank(message = "상품 상세설명은 필수 입력 값입니다.")
	private String itemDetail;	//상품 상세설명
	
	private ItemSellStatus itemSellStatus;	//상품 판매상태

	private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품의 이미지 정보 저장

	private List<Long> itemImgIds = new ArrayList<>(); //상품의 이미지 아이디 저장 -> 수정시 이미지 아이디를 담아 둘 용도

	private static ModelMapper modelMapper = new ModelMapper();
	
	public Item createItem() {
		//첫 번째 매개변수인 this는 현재 ItemFormDto 클래스 !
		return modelMapper.map(this, Item.class);
	}

	public static ItemFormDto of(Item item) {
		return modelMapper.map(item, ItemFormDto.class);
	}
}



