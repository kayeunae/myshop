package com.myshop.entity;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.repository.MemberRepository;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

	@Autowired
	MemberRepository memberRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("auditing 테스트")
	@WithMockUser(username = "gildong", roles ="USER") //지정한 사용자가 로그인 상태라고 가정한다. 스프링시큐리티에서 제공하는 어노테이션
	public void auditingTest() {
		Member newMember = new Member();
		memberRepository.save(newMember);
		
		em.flush();	//영속성컨텍스트에 flush
		em.clear();	//영속성컨텍스트를 초기화하면 DB에 직접 접근하기 때문에 DB에 제대로 잘 들어갔는지 확인이 가능함.
		
		Member member = memberRepository.findById(newMember.getId())
				.orElseThrow(EntityNotFoundException::new);
		
		System.out.println("등록시간: " + member.getRegTime());
		System.out.println("수정시간: " + member.getUpdateTime());
		System.out.println("등록한 사람: " + member.getCreateBy());
		System.out.println("수정한 사람: " + member.getModifiedBy());
	}
}
