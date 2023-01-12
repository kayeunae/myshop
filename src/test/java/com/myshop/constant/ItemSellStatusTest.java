package com.myshop.constant;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import com.myshop.entity.Item;
import com.myshop.entity.QItem;
import com.myshop.repository.ItemRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;


@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")	//test code 작성할 때 쓸 DB로 만든 property에 연결. 여기서는 H2 DB에 연결됨.
class ItemSellStatusTest {

	//의존성 주입
	//생성된 해당 클래스 타입 객체의 인스턴스를 연결한다. set함수의 역할을 하는 것과 같음
	@Autowired
	ItemRepository itemRepository;
	
	@PersistenceContext		//영속성컨텍스트를 사용하기 위해 선언
	EntityManager em;		//엔티티 매니저
	
	
	/*
//	@Test	//test할 메소드 위에 붙이는 어노테이션 (test 어노테이션이 붙지 않은 메소드는 그냥 메소드로 존재, 코드가 실행되지는 않음.)
//	@DisplayName("상품 저장 테스트")		//test할 메소드 네이밍. JUnit 콘솔창에 이 이름으로 떠서 구별이 쉬워짐 !
	public void createItemTest() {
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());	//LocalDateTime에서 현재 날짜와 시간 저장
		item.setUpdateTime(LocalDateTime.now());
		
		//insert
		Item savedItem = itemRepository.save(item);
		//save가 insert, update의 역할을 함.
		//JpaRepository에 있는 메소드라서 itemRepository가 상속받아 사용 가능.
	
		System.out.println(savedItem.toString());
	}
	*/
	
	public void createItemTest() {
		for(int i = 1; i <= 10; i++) {
 		Item item = new Item();
		item.setItemNm("테스트 상품" + i);
		item.setPrice(10000 + i);
		item.setItemDetail("테스트 상품 상세설명" + i);
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());	//LocalDateTime에서 현재 날짜와 시간 저장
		item.setUpdateTime(LocalDateTime.now());
		
		Item savedItem = itemRepository.save(item);	//데이터 insert
		}
	}
	
	public void createItemTest2() {
		for(int i = 1; i <= 5; i++) {
 		Item item = new Item();
		item.setItemNm("테스트 상품" + i);
		item.setPrice(10000 + i);
		item.setItemDetail("테스트 상품 상세설명" + i);
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
		item.setRegTime(LocalDateTime.now());	//LocalDateTime에서 현재 날짜와 시간 저장
		item.setUpdateTime(LocalDateTime.now());
		
		Item savedItem = itemRepository.save(item);	//데이터 insert
		}
		
		for(int i = 6; i <= 10; i++) {
	 		Item item = new Item();
			item.setItemNm("테스트 상품" + i);
			item.setPrice(10000 + i);
			item.setItemDetail("테스트 상품 상세설명" + i);
			item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
			item.setStockNumber(0);
			item.setRegTime(LocalDateTime.now());	//LocalDateTime에서 현재 날짜와 시간 저장
			item.setUpdateTime(LocalDateTime.now());
			
			Item savedItem = itemRepository.save(item);	//데이터 insert
			}
	}
	
	/*
	@Test
	@DisplayName("상품명 조회 테스트")
	public void findbyItemNmTest() {
		this.createItemTest();	//item 테이블에 insert
		List<Item> itemList = itemRepository.findByItemNm("테스트 상품2");
		// itemNM이 테스트 상품1 인 레코드를 반환할 것
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("상품명, 상품상세설명 or 테스트")
	public void findByItemNmOrItemDetail() {
		this.createItemTest();		//레코드 생성 메소드
		List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세설명5");
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("가격 LessThan 테스트")
	public void findByPriceLessThan() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByPriceLessThan(10005);
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("가격 내림차순 조회 테스트")
	public void findByPriceLessThanOrderByPriceDesc() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10004);
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈1-1 And")
	public void findByItemNmAndItemSellStatus() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByItemNmAndItemSellStatus("테스트 상품1", ItemSellStatus.SELL);
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈1-2 Between")
	public void findByPriceBetween() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByPriceBetween(10004, 10008);
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈1-3 After")
	public void findByRegTimeAfer() {
		this.createItemTest();
		//LocalDateTime time = LocalDateTime.of(2023, 01, 01, 12, 12, 44);
		//List<Item> itemList = itemRepository.findByRegTimeAfter(time);
		List<Item> itemList = itemRepository.findByRegTimeAfter(LocalDateTime.of(2023, 01, 01, 12, 12, 44));
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈1-4 isnotnull")
	public void findByItemSellStatusIsNotNull() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByItemSellStatusIsNotNull();
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("isnull")
	public void findByItemSellStatusIsNull() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByItemSellStatusIsNull();
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	@Test
	@DisplayName("퀴즈1-5 EndingWith")
	public void findByItemDetailEndingWith() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByItemDetailEndingWith("상세설명1");
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	*/
	/*
//	@Test
//	@DisplayName("@Query를 이용한 상품 조회 테스트")
	public void findByItemDetailTest() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByItemDetail("설명");
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
//	@DisplayName("@nativeQuery를 이용한 상품 조회 테스트")
	public void findByItemDetailByNativeTest() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByItemDetailByNative("설명");
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
//	@DisplayName("@Query 퀴즈2-1")
	public void findByPriceByNativeTest() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByPriceByNative(10005);
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	*/
	
	@Test
	@DisplayName("@Query 퀴즈2-2")
	public void findByItemNmAndSellTest() {
		this.createItemTest();
		List<Item> itemList = itemRepository.findByItemNmAndSell("테스트 상품1", ItemSellStatus.SELL);
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	
//	@Test
//	@DisplayName("querydsl 조회 테스트")
	public void queryDslTest() {
		this.createItemTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);	//JPAQueryFactory: 쿼리를 동적으로 생성하기 위한 객체
		QItem qItem = QItem.item;
		
		//쿼리문 생성
		//select * from item where itemSellStatus = 'SELL' and itemDetail like %상세설명% order by price desc
		JPAQuery<Item> query = qf.selectFrom(qItem)
								 .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
								 .where(qItem.itemDetail.like("%상세설명%"))
								 .orderBy(qItem.price.desc());
		
		//쿼리문 실행 (5가지 방법이 있음)
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	//검색 기능과 유사한 코드 수행
	@Test
	@DisplayName("querydsl 조회 테스트")
	public void queryDslTest2() {
		this.createItemTest2();
		JPAQueryFactory qf = new JPAQueryFactory(em);	//JPAQueryFactory: 쿼리를 동적으로 생성하기 위한 객체
		QItem qItem = QItem.item;
		Pageable page = PageRequest.of(0, 2);	//of(조회할 페이지의 번호, 한페이지당 조회할 데이터의 갯수)
		
		//쿼리문 생성
		//select * from item where itemSellStatus = 'SELL' and itemDetail like %상세설명% order by price desc
		JPAQuery<Item> query = qf.selectFrom(qItem)
								 .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
								 .where(qItem.itemDetail.like("%상세설명%"))
								 .where(qItem.price.gt(10003))
								 .offset(page.getOffset())
								 .limit(page.getPageSize());
		
		//쿼리문 실행 (5가지 방법이 있음)
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
	/* 코드 오류 
	//검색 기능과 유사한 코드 수행
	@Test
	@DisplayName("querydsl 조회 테스트2")
	public void queryDslTest2() {
		this.createItemTest2();
		BooleanBuilder b = new BooleanBuilder();	//쿼리에 들어갈 조건을 만들어주는 빌더
		QItem item = QItem.item;
		
		String itemDetail = "테스트 상품 상세설명";
		int price = 10003;
		String itemSellStat = "SELL";
		
		//select * from item 부분은 생략이 되어 있다고 생각하면 됨. 그냥 조건만 부여
		b.and(item.itemDetail.like("%" + itemDetail + "%"));
		b.and(item.price.gt(price));	//gt: Greater than (~보다 큰), 여기서는 price > 10003
		
		if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
			b.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
		}
		// ---BooleanBuilder를 통해 쿼리문 작성. 이후부터 페이징 들어감.
		
		Pageable page = PageRequest.of(1, 3);	//of(조회할 페이지의 번호, 한 페이지당 조회할 데이터의 갯수)
		Page<Item> ItemPageResult = itemRepository.findAll(b, page);	//findAll은 itemRepository가 JpaRepository를 상속받는 인터페이스라서 가져올 수 있음.
	}
	*/
	
	
	//퀴즈3
//	@Test
//	@DisplayName("퀴즈3-1")
	public void getNmAndSellState()	{
		this.createItemTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		
		JPAQuery<Item> query = qf.selectFrom(qItem)
								 .where(qItem.itemNm.eq("테스트 상품1"))
								 .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL));
								 
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
//	@DisplayName("퀴즈3-2")
	public void getPriceBetween()	{
		this.createItemTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		
		JPAQuery<Item> query = qf.selectFrom(qItem)
								 .where(qItem.price.between(10004, 10008));
								 
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
//	@DisplayName("퀴즈3-3")
	public void AfterRegTime()	{
		this.createItemTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		
		LocalDateTime regTime = LocalDateTime.of(2023, 1, 1, 12, 12, 44);
		
		JPAQuery<Item> query = qf.selectFrom(qItem)
								 .where(qItem.regTime.after(regTime));
								 
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
//	@DisplayName("퀴즈3-4")
	public void SellStateNotNull()	{
		this.createItemTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		
		JPAQuery<Item> query = qf.selectFrom(qItem)
								 .where(qItem.itemSellStatus.isNotNull());
								 
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
//	@Test
//	@DisplayName("퀴즈3-5")
	public void itemDetailEnding()	{
		this.createItemTest();
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QItem qItem = QItem.item;
		
		JPAQuery<Item> query = qf.selectFrom(qItem)
								 .where(qItem.itemDetail.endsWith("설명1"));
							   //.where(qItem.itemDetail.like("설명1"));
								 
		List<Item> itemList = query.fetch();
		
		for(Item item : itemList) {
			System.out.println(item.toString());
		}
	}
	
}