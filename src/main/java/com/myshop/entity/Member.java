package com.myshop.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.myshop.constant.Role;
import com.myshop.dto.MemberFormDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//회원가입 정보를 저장할 엔티티 클래스

@Entity
@Table(name="member") //매핑할 테이블명 설정. 설정을 하지 않으면 클래스명으로 설정됨
@Getter
@Setter
@ToString
public class Member extends BaseEntity {
	
	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Column(unique = true)	//유니크 제한자 걸어줄 때
	private String email;
	
	private String password;
	
	private String address;
	
	@Enumerated(EnumType.STRING)	//enum 클래스 사용할 때
	private Role role;
	
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		
		//패스워드는 암호화해줘야 하기 때문에 한 번 더 처리해줘야 한다.
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		member.setPassword(password);
		
		member.setRole(Role.USER);
		
		return member;
	}
}
