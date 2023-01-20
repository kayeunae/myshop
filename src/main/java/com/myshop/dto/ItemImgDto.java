package com.myshop.dto;

import org.modelmapper.ModelMapper;

import com.myshop.entity.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {
	private Long id;
	
	private String imgName; //이미지 파일명
	
	private String oriImgName; //원본 이미지 파일명
	
	private String imgUrl; //이미지 조회 경로
	
	private String repimgYn; //대표 이미지 여부 
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static ItemImgDto of(ItemImg itemImg) {	//엔티티 클래스와 dto클래스를 효율적으로 변환시켜줌
		return modelMapper.map(itemImg, ItemImgDto.class);
	}
}
