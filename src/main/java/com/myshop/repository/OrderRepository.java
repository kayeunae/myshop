package com.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
