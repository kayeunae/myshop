package com.myshop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.myshop.constant.OrderStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="orders")
@Getter
@Setter
@ToString
public class Order {
	
	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	private LocalDateTime orderDate;	//주문일
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;	//주문상태
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)	//OrderItem에 있는 order에 의해 관리가 된다.
	private List<OrderItem> orderItems = new ArrayList<>();	//orderItem이 다수이기 때문에 List<>로 가져온다.

	//orderItems에 넣어줌
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);	//OrderItem 리스트에 orderItem 객체를 넣어줌
		orderItem.setOrder(this);	//양방향 맵핑을 해놨으면 add 후에 orderItem 객체에도 order(this)객체를 set 해줘야 함 (★양방향 참조관계-서로가 서로를 참조하는 관계-일 때는 orderItem객체에도 order객체를 세팅한다.)
	}
	
	//order 객체를 생성
	public static Order createOrder(Member member, List<OrderItem> orderItemList) {
		Order order = new Order();
		order.setMember(member); //order와 member도 관계성이 있으므로 넣어줘야 함.
		
		for(OrderItem orderItem : orderItemList) {
			order.addOrderItem(orderItem);
		}
		
		//주문 상태를 바꿔줌
		order.setOrderStatus(OrderStatus.ORDER);
		//주문 날짜를 현재 날짜로 설정해줌
		order.setOrderDate(LocalDateTime.now());
		
		return order;
	}

	//총 주문금액
	public int getTotalPrice() {
		int totalPrice = 0;
		for(OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getTotalPrice();
		}
		return totalPrice;
	}
}
