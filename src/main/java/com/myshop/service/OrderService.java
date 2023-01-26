package com.myshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.dto.OrderDto;
import com.myshop.entity.Item;
import com.myshop.entity.Member;
import com.myshop.entity.Order;
import com.myshop.entity.OrderItem;
import com.myshop.repository.ItemRepository;
import com.myshop.repository.MemberRepository;
import com.myshop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

//주문 정보를 가공하여 넘겨주는 Service 클래스

@Service	//service 클래스의 역할을 한다!
@Transactional	//서비스 클래스에서 로직을 처리하다가 에러가 발생하면 로직을 수행하기 이전 상태로 되돌려 준다.
@RequiredArgsConstructor
public class OrderService {
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	
	public Long order(OrderDto orderDto, String email) {
		Item item = itemRepository.findById(orderDto.getItemId())
				.orElseThrow(EntityNotFoundException::new);
		
		Member member = memberRepository.findByEmail(email);
		
		List<OrderItem> orderItemList = new ArrayList<>();
		OrderItem orderItem = OrderItem.createOrederItem(item, orderDto.getCount());
		orderItemList.add(orderItem);
		
		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order);
		
		return order.getId();
	}
}
