package com.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
