package practice.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import practice.model.Account;
import practice.repository.AccountRepository;
import practice.repository.UserRepository;

@Slf4j
@Controller
@RequestMapping("/authen")
@SessionAttributes("currentAccount")
public class AuthenController {
	
	@Autowired
	private AccountRepository accountRepo;
	
	@ModelAttribute("currentAccount")
	public Account account() {
		return new Account();
	}
	
	@GetMapping
	public String getUser(
			@ModelAttribute("currentAccount") Account currentAccount
			) {
		String currentUsername = SecurityContextHolder
				.getContext().getAuthentication().getName();
		Account account = accountRepo.findByUsername(currentUsername).orElse(null);
		if(account!=null) {
			currentAccount.setup(account);
		}
		return "redirect:/";
	}
}
