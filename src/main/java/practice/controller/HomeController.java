package practice.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import practice.model.Account;

@Slf4j
@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model, HttpSession session) {
		if (session.getAttribute("currentAccount") != null) {
			model.addAttribute("account", session.getAttribute("currentAccount"));
		}
		return "homepage";
	}

	@GetMapping("/login")
	public String login(HttpSession session) {
		if (session.getAttribute("currentAccount") != null) {
			return "logout";
		}
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		Account account = (Account) session.getAttribute("currentAccount");
		if (account != null) {
			log.info("Log out: " + account);
		}
		return "logout";
	}

}
