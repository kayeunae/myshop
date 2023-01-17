package com.myshop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc	//실제 객체와 비슷하지만 테스트에 필요한 기능만 제공하는 가짜 객체를 만든다. => 웹브라우저에서 요청을 하는 것처럼 작성 가능
@TestPropertySource(locations="classpath:application-test.properties")	//test code 작성할 때 쓸 DB로 만든 property에 연결. 여기서는 H2 DB에 연결됨.
class ItemControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	@DisplayName("상품 등록 페이지 권한 테스트")
	@WithMockUser(username = "admin", roles="ADMIN")	//가상의 유저를 만들어줌(유저가 로그인된 상태로 테스트 할 수 있게 해준다.)
	public void itemFormTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))	//get 방식으로 리퀘스트를 한다.
		.andDo(print())	//요청과 응답 메세지를 콘솔에서 출력
		.andExpect(status().isOk());	//응답 상태 코드가 정상인지 확인 후 정상이면 테스트 통과
	}
	
	@Test
	@DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
	@WithMockUser(username = "admin", roles="USER")	//가상의 유저를 만들어줌(유저가 로그인된 상태로 테스트 할 수 있게 해준다.)
	public void itemFormNotAdminTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))	//get 방식으로 리퀘스트를 한다.
		.andDo(print())	//요청과 응답 메세지를 콘솔에서 출력
		.andExpect(status().isForbidden());	//응답 상태 코드에서 Forbidden 예외가 발생하면 테스트가 성공적으로 통과된다.
	}
}
