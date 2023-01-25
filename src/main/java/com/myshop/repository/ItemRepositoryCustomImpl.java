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
import com.myshop.dto.MainItemDto;
import com.myshop.dto.QMainItemDto;
import com.myshop.entity.Item;
import com.myshop.entity.QItem;
import com.myshop.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
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
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("itemNm", searchBy)) {
			return QItem.item.itemNm.like("%" + searchQuery + "%"); //itemNm LIKE %청바지%
		} else if(StringUtils.equals("createdBy", searchBy)) {	//createBy LIKE %test.com%
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
						searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())) // and itemNm(or createBy) LIKE %검색어%
				.orderBy(QItem.item.id.desc())
				.offset(pageable.getOffset())	//데이터를 가져올 시작 index
				.limit(pageable.getPageSize())	//한 번에 가지고 올 최대 개수
				.fetch();
		
//		long total = content.size(); size로 구하면 오류가 나서 쿼리문으로 구현해야 함
		//Wildcard.count = count(*)
		long total = queryFactory.select(Wildcard.count).from(QItem.item)	//전체 레코드의 갯수를 구함
				.where(regDtsAfter(itemSearchDto.getSearchDateType()),
						searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
						searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
				.fetchOne();
		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression itemNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
	}
	
	@Override
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		//QItem과 QItemImg 불러온다.
		QItem item = QItem.item;
		QItemImg itemImg = QItemImg.itemImg;
		
		List<MainItemDto> content = queryFactory.select(
				new QMainItemDto (	//MainItemDto 객체로 저장. (@QueryProjection)
						item.id,
						item.itemNm,
						item.itemDetail,
						itemImg.imgUrl,
						item.price)
				)
				.from(itemImg)
				.join(itemImg.item, item)	//item을 통해서 두 테이블 join
				.where(itemImg.repimgYn.eq("Y"))
				.where(itemNmLike(itemSearchDto.getSearchQuery()))
				.orderBy(item.id.desc())
				.offset(pageable.getOffset())	//데이터를 가져올 시작 index
				.limit(pageable.getPageSize())	//한 번에 가져올 데이터의 최대 개수
				.fetch();
		
		//count(*), count할 때는 fecthOne으로 끝내면 된다. 강의교안 참고 !
		//레코드의 갯수를 구하는 메소드 !
		long total = queryFactory.select(Wildcard.count)
				.from(itemImg)
				.join(itemImg.item, item)
				.where(itemImg.repimgYn.eq("Y"))
				.where(itemNmLike(itemSearchDto.getSearchQuery()))
				.fetchOne();
				
		//이렇게 리턴 시키면 스프링이 알아서 Page 객체를 만들어준다.
		return new PageImpl<>(content, pageable, total);
	}

}
