package com.myshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;

//JpaRepository : 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있다.
//JpaRepository<사용할 엔티티 클래스, 엔티티 클래스의 기본 키 타입>
public interface ItemRepository extends JpaRepository<Item, Long>, 
	   QuerydslPredicateExecutor<Item>, ItemRepositoryCustom  {
	
	//JPA 쿼리 메소드(메소드명은 샘플을 참고하여 항상 맞춰서 적어줘야 한다.)
	
	//select * from item where item_nm = ? (물음표 자리는 매개변수로 받음)
	//find 메소드는 where의 역할. 사용은 test code에서 !
	List<Item> findByItemNm(String itemNm);
	
	//select * from item where item_nm ? or item_detail = ?
	List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
	
	//select * from item where price < ?
	List<Item> findByPriceLessThan(Integer price);

	//select * from item where price < ? order by price desc
	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
	
	/*
	//퀴즈1
	//select * from item where itemNm = ? and itemSellStatus = ?
	List<Item> findByItemNmAndItemSellStatus(String itemNm, ItemSellStatus itemSellStatus);

	//select * from item where price ? and ?
	List<Item> findByPriceBetween(Integer price1, Integer price2);
	
	//select * from item where regTime > ?
	List<Item> findByRegTimeAfter(LocalDateTime regTime);

	//select * from item where itemSellStatus is not null
	List<Item> findByItemSellStatusIsNotNull();
	
	//select * from item where itemSellStatus is null
	List<Item> findByItemSellStatusIsNull();
	
	//select * form item where itemDetail like %?
	List<Item> findByItemDetailEndingWith(String itemDetail);
	*/
	
	//JPQL @Query 어노테이션
	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item> findByItemDetail(@Param("itemDetail")String itemDetail);
	
	//@Query("select i from Item i where i.itemDetail like %?1% order by i.price desc")
	//List<Item> findByItemDetail(String itemDetail);
	
	@Query(value = "select * from Item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
	
	/*
	//퀴즈2
	@Query("select i from Item i where i.price >= :price")
	List<Item> findByPriceByNative(@Param("price") Integer price);
	
	@Query("select i from Item i where i.itemNm = :itemNm and i.itemSellStatus = :sell")
	List<Item> findByItemNmAndSell(@Param("itemNm") String itemNm, @Param("sell") ItemSellStatus sell);
	*/
	
	@Query(value = "select * from Item i where i.item_nm = :itemNm and i.item_sell_status = :#{#sell.name()}", nativeQuery = true)
	List<Item> findByItemNmAndSell(@Param("itemNm") String itemNm, @Param("sell") ItemSellStatus sell);
	
	
	
}

