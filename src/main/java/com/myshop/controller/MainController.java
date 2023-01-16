package com.myshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	//루트 경로 !
	@GetMapping(value = "/")
	public String main() {
		return "main";
	}
}



