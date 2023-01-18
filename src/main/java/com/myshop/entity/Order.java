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
}
