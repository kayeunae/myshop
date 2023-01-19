package com.myshop.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.constant.ItemSellStatus;
import com.myshop.repository.ItemRepository;
import com.myshop.repository.MemberRepository;
import com.myshop.repository.OrderItemRepository;
import com.myshop.repository.OrderRepository;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ItemRepository itemRepository;

	@PersistenceContext
	EntityManager em;

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;

	public Item createItemTest() {
		Item item = new Item();
		item.setItemNm("테스트 상품");
		item.setPrice(10000);
		item.setItemDetail("테스트 상품 상세 설명");
		item.setItemSellStatus(ItemSellStatus.SELL);
		item.setStockNumber(100);
//		item.setRegTime(LocalDateTime.now());
//		item.setUpdateTime(LocalDateTime.now());

		return item;
	}

	@Test
	@DisplayName("영속성 전이 테스트")
	public void cascadeTest() {
		Order order = new Order();

		for (int i = 0; i < 3; i++) {
			Item item = this.createItemTest(); // 3개의 상품 생성(물건이 있어야 주문을 할 수 있으므로 먼저 상품을 생성해준다.)
			itemRepository.save(item);

			// 생성한 상품을 order_item에 넣어준다. -> 주문 상태로 만들어줌(주문 상황)
			OrderItem orderItem = new OrderItem();
			orderItem.setItem(item);
			orderItem.setCount(10);
			orderItem.setOrderPrice(1000);
			orderItem.setOrder(order); // order_item에 order를 넣어줌

			order.getOrderItems().add(orderItem); // 객체 배열에 객체를 넣어줌
		}
		orderRepository.saveAndFlush(order); // 영속성 컨텍스트에 order 엔티티를 저장함과 동시에 flush함
		em.clear(); // 영속성 컨텍스트 초기화

		Order saveOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);

		assertEquals(3, saveOrder.getOrderItems().size()); // orderitem이 list<>이므로 .size()로 개수를 확인
	}

	//회원이 주문하는 단계
	public Order createOrder() {
		Order order = new Order();

		for (int i = 0; i < 3; i++) {
			Item item = this.createItemTest(); // 3개의 상품 생성(물건이 있어야 주문을 할 수 있으므로 먼저 상품을 생성해준다.)
			itemRepository.save(item);

			// 생성한 상품을 order_item에 넣어준다. -> 주문 상태로 만들어줌(주문 상황)
			OrderItem orderItem = new OrderItem();
			orderItem.setItem(item);
			orderItem.setCount(10);
			orderItem.setOrderPrice(1000);
			orderItem.setOrder(order); // order_item에 order를 넣어줌

			order.getOrderItems().add(orderItem); // 객체 배열에 객체를 넣어줌
		}
		
		Member member = new Member();
		memberRepository.save(member);
		
		order.setMember(member);
		orderRepository.save(order);
		
		return order;
	}
	
	@Test
	@DisplayName("고아 객체 제거 테스트")
	public void orphanRemovalTest() {
		Order order = this.createOrder();
		order.getOrderItems().remove(0); //주문 엔티티에서 주문상품 엔티티(자식)를 삭제 했을 때 orderItem 엔티티가 삭제된다.
		em.flush();
	}
	
	@Test
	@DisplayName("지연 로딩 테스트")
	public void lazyLoadingTest() {
		Order order = this.createOrder();
		//getOrderItems(): 주문한 물건
		//getOrderItems().get(0) : 주문한 물건 중 첫 번째 물건
		//getOrderItems().get(0).getId() : 그 물건의 ID
		// => order_item 테이블의 id를 구하는 것
		Long orderItemId = order.getOrderItems().get(0).getId();
		
		em.flush();
		em.clear();
		
		OrderItem orderItem = orderItemRepository.findById(orderItemId)
				.orElseThrow(EntityNotFoundException::new);
	}
}
