package com.myshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.myshop.constant.ItemSellStatus;
import com.myshop.dto.ItemSearchDto;
import com.myshop.entity.Item;
import com.myshop.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
	
	//의존성 주입
	private JPAQueryFactory queryFactory;
	
	//의존성 주입 - 생성자방식
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();	//현재 날짜와 시간(검색 당시)
		
		//현재 날짜로부터 이전 날짜를 구해준다.
		if(StringUtils.equals("all", searchDateType) || searchDateType == null) return null;
		else if(StringUtils.equals("1d", searchDateType)) dateTime = dateTime.minusDays(1);
		else if(StringUtils.equals("1w", searchDateType)) dateTime = dateTime.minusWeeks(1);
		else if(StringUtils.equals("1m", searchDateType)) dateTime = dateTime.minusMonths(1);
		else if(StringUtils.equals("6m", searchDateType)) dateTime = dateTime.minusMonths(6);
	
		//QItem.item.regTime.after(dateTime); : 동적 쿼리
		return QItem.item.regTime.after(dateTime);	//이후의 시간
	}
	
	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
	}
	
	private BooleanExpression SearchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("itemNm", searchBy)) {
			return QItem.item.itemNm.like("%" + searchQuery + "%"); //itemNm LIKE %청바지%
		} else if(StringUtils.equals("createdBy", searchBy)) {	//createBy LIKI %test.com%
			return QItem.item.createdBy.like("%" + searchQuery + "%");
		}
		
		return null;
	}
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		List<Item> content = queryFactory
				.selectFrom(QItem.item)	//select * from item
				.where(regDtsAfter(itemSearchDto.getSearchDateType()), //where reg_time = ?
						searchSellStatusEq(itemSearchDto.getSearchSellStatus()),	//and sell status = ?
						SearchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())) // and itemNm(or createBy) LIKE %검색어%
				.orderBy(QItem.item.id.desc())
				.offset(pageable.getOffset())	//데이터를 가져올 시작 index
				.limit(pageable.getPageSize())	//한 번에 가지고 올 최대 개수
				.fetch();
		
		long total = content.size();
		
		return new PageImpl<>(content, pageable, total);
	}

}
