package com.myshop.dto;

import com.myshop.constant.ItemSellStatus;

import lombok.*;

@Getter
@Setter
public class ItemSearchDto {
	private String searchDateType;
	private ItemSellStatus searchSellStatus;
	private String searchBy;
	private String searchQuery ="";
}



