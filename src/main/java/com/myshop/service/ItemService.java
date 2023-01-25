package com.myshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.myshop.dto.ItemFormDto;
import com.myshop.dto.ItemImgDto;
import com.myshop.dto.ItemSearchDto;
import com.myshop.dto.MainItemDto;
import com.myshop.entity.Item;
import com.myshop.entity.ItemImg;
import com.myshop.repository.ItemImgRepository;
import com.myshop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImgService itemImgService;
	private final ItemImgRepository itemImgRepository;
	
	//상품등록
	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
		
		//상품 등록
		Item item = itemFormDto.createItem();
		itemRepository.save(item);
	
		//이미지 등록
		for(int i=0; i<itemImgFileList.size(); i++) {
			ItemImg itemImg = new ItemImg();
			itemImg.setItem(item);
			
			//첫 번째 이미지를 대표 이미지로 지정
			if(i == 0) {
				itemImg.setRepimgYn("Y");
			} else {
				itemImg.setRepimgYn("N");
			}
			
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
			return item.getId();
	}
	
	//상품 가져오기
	@Transactional(readOnly = true)	//트랜잭션 읽기 전용. select 구문은 성능 향상을 위해 읽기 전용으로 설정
	public ItemFormDto getItemDtl(Long itemId) {
		//1. item_img 테이블의 이미지를 가져온다.
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); //등록된 이미지를 불러온다.
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		
		//엔티티 객체 -> DTO 객체로 변환
		for(ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg); //modelMapper로 자원을 효율적으로 이용. (엔티티와 Dto 변수명이 같으므로 알아서 매핑해줌)
			itemImgDtoList.add(itemImgDto);
		}
		
		//2. item 테이블에 있는 데이터를 가져온다.
		Item item = itemRepository.findById(itemId)
								  .orElseThrow(EntityNotFoundException::new); //리턴 타입이 optional이기 때문에 orElseThrow 필수 !
		
		//엔티티 객체 -> DTO 객체로 변환
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		
		//상품의 이미지 정보를 넣어준다.
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		
		return itemFormDto;
	}
	
	//상품 수정
	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
		Item item = itemRepository.findById(itemFormDto.getId())
								  .orElseThrow(EntityNotFoundException::new);
	
	item.updateItem(itemFormDto);	//수정한 데이터를 넣어준다. 이미 영속성 컨텍스트에 있는 엔티티이므로 save를 해주지 않아도 됨
	
	List<Long> itemImgids = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트를 조회
	
	for(int i=0; i<itemImgFileList.size(); i++) {
		itemImgService.updateItemImg(itemImgids.get(i), itemImgFileList.get(i));
	}
	
	return item.getId();
	
	}
	
	//상품 리스트 가져오기
	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getAdminItemPage(itemSearchDto, pageable); 
	}
	
	//메인 화면에서 상품 리스트 가져오기
	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getMainItemPage(itemSearchDto, pageable); 
	}
	
}