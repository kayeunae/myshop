package com.myshop.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myshop.dto.OrderDto;
import com.myshop.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	//의존성 주입
	private final OrderService orderService;
	
	@PostMapping(value = "/order")
	public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, 
			BindingResult bindingResult, Principal principal) {		//Principal : 로그인한 사용자의 정보를 얻어올 수 있음.
		
		//바인딩 에러 처리 (비동기 방식에서 에러 처리하는 방법 !)
		if(bindingResult.hasErrors()) {
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();	//에러목록을 전부 가져와 list에 넣어준다.
			
			for(FieldError fieldError : fieldErrors) {
				sb.append(fieldError.getDefaultMessage());
			}
			
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		String email = principal.getName(); // SecurityConfig에서 userNameParameter를 email로 지정했음.
		Long orderId;
		
		try {
			orderId = orderService.order(orderDto, email);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		//참고) ResponseEntity는 보내는 곳의 타입에 맞춰주면 됨.
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}
}
