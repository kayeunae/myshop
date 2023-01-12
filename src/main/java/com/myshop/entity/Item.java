package com.myshop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.Temporal;

import com.myshop.constant.ItemSellStatus;

import lombok.*;

@Entity
@Table(name="item") //매핑할 테이블명 설정. 설정을 하지 않으면 클래스명으로 설정됨
@Getter
@Setter
@ToString
public class Item {
	//Not null이 아닐 때는 필드 타입을 객체 (예. int - Integer)로 지정해야 한다.
	
	@Id
	@Column(name="item_id")
	@GeneratedValue(strategy = GenerationType.AUTO) 		
	private Long id;	//상품코드
	
	@Column(nullable = false, length = 50)
	private String itemNm;	//상품명
	
	@Column(nullable = false, name="price")
	private int price;	//가격

	@Column(nullable = false)
	private int stockNumber;	//재고수량
	
	@Lob
	@Column(nullable = false)
	private String itemDetail;	//상품 상세설명
	
	@Enumerated(EnumType.STRING)	//값을 그대로 저장. ORDINAL은 인덱스 번호로 저장. 열거형을 사용하면 해당 컬럼은 enum에 설정한 값만을 가질 수 있음. 여기서는 둘 중 하나만 !
	private ItemSellStatus itemSellStatus;	//상품 판매상태

	private LocalDateTime regTime;	//등록 시간
	
	private LocalDateTime updateTime;	//수정 시간
}
