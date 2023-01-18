package com.myshop.entity;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.dto.MemberFormDto;
import com.myshop.repository.CartRepository;
import com.myshop.repository.MemberRepository;
import com.myshop.service.MemberService;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CartTest {
	@Autowired
	CartRepository cartRepository;

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PersistenceContext
	EntityManager em;
	
	public Member createMember() {
		MemberFormDto member = new MemberFormDto();
		member.setName("홍길동");
		member.setEmail("test@email.com");
		member.setAddress("서울시 마포구 합정동");
		member.setPassword("1234");
		
		return Member.createMember(member, passwordEncoder);
	}
	
	@Test
	@DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
	public void findCartAndMemberTest() {
		Member member = createMember();	//멤버 생성
		memberRepository.save(member);	//save(): 기존에 있는 것은 수정(update), 없는 것은 저장(insert)
		
		Cart cart = new Cart();
		cart.setMember(member);	//cart에 새로 생성한 member 객체를 넣어준다.
		cartRepository.save(cart); //member_id가 cart의 fk로 들어가서 관계성을 맺는다.
		
		em.flush();	//트랜잭션이 끝날 때 데이터 베이스에 반영. findCartAndMemberTest()의 코드에서 flush()가 실행되기 전에는 DB에 반영되지 않은 상태임. 엔티티가 컨텍스트에 저장된 상태('쓰기 지연 저장소')에서 flush(트랜잭션 commit 시점에 flush)를 하면 sql문이 반영됨
		em.clear();	//영속성 컨텍스트를 비워준다. 왜? 실제 데이터 베이스에서 장바구니 엔티티를 가지고 올 때 회원 엔티티도 같이 가지고 오는지 보기 위해. (영속성 컨텍스트에 엔티티가 저장되었다가 분리된 상태)
	
		Cart savedCart = cartRepository.findById(cart.getId()) //findById : return 값으로 optional을 받는다.
				.orElseThrow(EntityNotFoundException::new);	//에러 처리를 간단하게 해줌
		//값 비교
		assertEquals(savedCart.getMember().getId(), member.getId());
	}
}




