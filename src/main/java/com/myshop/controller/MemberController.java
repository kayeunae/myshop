package com.myshop.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myshop.dto.MemberFormDto;
import com.myshop.entity.Member;
import com.myshop.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
	//의존성 주입
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	//get 방식으로 받을 때
	//회원가입 화면을 보여준다.
	@GetMapping(value = "/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm"; //memberForm html 페이지를 띄워줌
	}
	
	//post 방식으로 받을 때
	//회원가입 버튼을 눌렀을 때 실행되는 메소드
	@PostMapping(value = "/new")
	public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		//@Valid : 유효성을 검증하려는 객체 앞에 붙인다.
		//bindingResult : 유효성 검증 후에 결과를 bindingResult에 넣어준다.
		
		//에러가 있다면 회원가입 페이지로 이동
		if(bindingResult.hasErrors()) {	//하나라도 에러가 발견되면 true
			return "member/memberForm";
		}
		
		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (Exception e) {
			//memberForm.html의 script에 있는 errorMessage로 간다.
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}
		
		return "redirect:/";
		//redirect 데이터를 수정하거나 새로 등록할 때는 redirect를 써줘야 한다.
		//이걸 하지 않으면 get 방식으로 하기 때문에 새로고침하거나 할 때 데이터가 또 들어갈 수 있기 때문에.
		//리다이렉트는 새로고침을 하게 되면 안에 들어있던 데이터를 전부 날리고 처음부터 새로 다시 하는 것 !
		// redirect:/ 에서 '/'는 루트 경로! 바로 localhost로(메인으로) 간다.
	}
}
