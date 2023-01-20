package com.myshop.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.myshop.entity.ItemImg;
import com.myshop.repository.ItemImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
	
	@Value("${itemImgLocation}")
	private String itemImgLocation;
	
	private final ItemImgRepository itemImgRepository;
	
	private final FileService fileService;
	
	//이미지 저장
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
		String oriImgName = itemImgFile.getOriginalFilename();	//파일의 원본 이름을 가져옴
		String imgName = "";
		String imgUrl = "";
		
		//파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) { //oriImgName이 null이거나 빈 문자열이 아닐 경우
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/images/item/" + imgName;
		}
		
		//상품 이미지 정보 저장
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(itemImg); //영속성 컨텍스트에 저장 > DB에 업로드
		//mysql은 auto commit 기능이 있어서 flush를 하지 않고 save까지만 해줘도 됨
	}
	
	//이미지 업데이트 메소드
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
		if(!itemImgFile.isEmpty()) {	//파일이 있으면
			ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
													.orElseThrow(EntityNotFoundException::new);
			
			//기존 이미지 파일 삭제 (이미지 재업로드 전에 삭제를 먼저 해줘야 함)
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {  //savedItemImg에서 getImgName이 있으면
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg); //이 경로에 있는 파일을 삭제함. 전체 경로 -> C:/shop/item/이미지 이름
			}
			
			//이미지 파일 업로드
			String oriImgName = itemImgFile.getOriginalFilename();	//파일의 원본 이름을 가져옴
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			String imgUrl = "/images/item/" + imgName;
			
			//★saveItemImg()의 itemImgRepository.save(itemImg); 를 적지 않아도 되는 이유 ?
			//savedItmeImg는 현재 영속상태(영속성 컨텍스트에 이 엔티티가 들어있음)
			//처음 insert할 때는 영속상태로 만들어줘야 하지만, 데이터를 변경하는 것만으로 변경 감지 기능이 동작하여 트랜잭션이 끝날 때 update 쿼리가 실행된다.
			// -> 엔티티가 반드시 영속상태여야 한다.
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
		}
	}
}
