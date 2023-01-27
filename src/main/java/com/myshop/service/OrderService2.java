package com.myshop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.dto.OrderDto;
import com.myshop.dto.OrderHistDto;
import com.myshop.dto.OrderItemDto;
import com.myshop.entity.Item;
import com.myshop.entity.ItemImg;
import com.myshop.entity.Member;
import com.myshop.entity.Order;
import com.myshop.entity.OrderItem;
import com.myshop.repository.ItemImgRepository;
import com.myshop.repository.ItemRepository;
import com.myshop.repository.MemberRepository;
import com.myshop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

//주문 정보를 가공하여 넘겨주는 Service 클래스

@Service	//service 클래스의 역할을 한다!
@Transactional	//서비스 클래스에서 로직을 처리하다가 에러가 발생하면 로직을 수행하기 이전 상태로 되돌려 준다.
@RequiredArgsConstructor
public class OrderService2 {
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	private final ItemImgRepository itemImgRepository;
	
	public Long order(OrderDto orderDto, String email) {
		Item item = itemRepository.findById(orderDto.getItemId())
				.orElseThrow(EntityNotFoundException::new);
		
		Member member = memberRepository.findByEmail(email);
		
		List<OrderItem> orderItemList = new ArrayList<>();
//		OrderItem orderItem = OrderItem.createOrederItem(item, orderDto.getCount());
//		orderItemList.add(orderItem);
		
		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order);
		
		return order.getId();
	}
	
	@Transactional(readOnly = true)
	public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
		
		List<Order> orders = orderRepository.findOrders(email, pageable); //주문 목록
		Long totalCount = orderRepository.countOrder(email); //총 주문 목록 개수
		
		List<OrderHistDto> orderHistDtos = new ArrayList<>();	//주문 목록을 담을 리스트
		
		//상품의 종류가 여러가지이므로 전부 불러올 수 있도록 코드 작성해줌
		for(Order order : orders) {
			OrderHistDto orderHistDto= new OrderHistDto(order);
			List<OrderItem> orderItems = order.getOrderItems();
			
			//대표 이미지를 가져와 처리(주문 목록에서 대표 이미지 띄워줄 것)
			for(OrderItem orderItem : orderItems) {
				ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
				OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
				orderHistDto.addOrderItemDto(orderItemDto);
			}
			
			orderHistDtos.add(orderHistDto);
		}
		
		return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
	}
}
