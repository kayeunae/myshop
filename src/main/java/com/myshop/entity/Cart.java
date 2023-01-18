package com.myshop.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.myshop.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="cart") //테이블명
@Getter
@Setter
@ToString
public class Cart {

	@Id
	@Column(name="cart_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
}
