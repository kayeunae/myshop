package com.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
