package practice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import practice.model.Client;
import practice.model.User;
import practice.model.Account;
import practice.repository.AccountRepository;
import practice.repository.ClientRepository;
import practice.repository.UserRepository;

@Slf4j
@Controller
@RequestMapping("/account")
@SessionAttributes({"addedUser", "addedClient"})
public class AccountController {

	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ClientRepository clientRepo;
	
	@ModelAttribute("addedUser")
	public User addedUser() {
		return new User();
	}
	
	@ModelAttribute("addedClient")
	public Client addedClient() {
		return new Client();
	}
	
	@ModelAttribute("addedAccount")
	public Account addedAccount() {
		return new Account();
	}

	@GetMapping
	public String account(Model model, HttpSession session) {
		if (session.getAttribute("currentAccount") != null) {
			Account account = (Account) session.getAttribute("currentAccount");
			model.addAttribute("account", account);
		}
		return "account";
	}

	@GetMapping("/submitInfo")
	public String submitInfo(Model model) {
		model.addAttribute("user", new User());
		return "submitInfo";
	}
	
	@PostMapping("/submitInfo")
	public String submitClientInfo(Model model, @Valid User user, Errors errors, @SessionAttribute("addedUser") User addedUser) {
		if(errors.hasErrors()) {
			return "submitInfo";
		}
		addedUser.setFullname(user.getFullname());
		addedUser.setIdCard(user.getIdCard());
		addedUser.setPhoneNumber(user.getPhoneNumber());
		addedUser.setAddress(user.getAddress());
		addedUser.setEmail(user.getEmail());
		addedUser.setNote(user.getNote());
		addedUser.setRole("Khách hàng");
		
		model.addAttribute("client", new Client());
		return "submitClient";
	}
	
	@PostMapping("/submitClient")
	public String createAccount(Model model, @Valid Client client, Errors errors, @SessionAttribute("addedClient") Client addedClient) {
		if(errors.hasErrors()) {
			return "submitClient";
		}
		
		addedClient.setBankAccount(client.getBankAccount());
		addedClient.setNote(client.getNote());
		model.addAttribute("account", new Account());
		return "createAccount";
	}

	@PostMapping("/create")
	public String registerAccount(@Valid Account account, Errors errors, HttpSession session) {
		if(errors.hasErrors()) {
			return "createAccount";
		}
		
		User addedUser = (User) session.getAttribute("addedUser");
		Client addedClient = (Client) session.getAttribute("addedClient");

		
		userRepo.save(addedUser);
		account.setActive(true);
		account.setRoles("ROLE_USER");
		account.setUser(addedUser);
		accountRepo.save(account);
		addedClient.setUser(addedUser);
		clientRepo.save(addedClient);
//		log.info("Account: " + account);
//		log.info("User: " + addedUser);
//		log.info("Client: " + addedClient);
		return "redirect:/";
	}

	@GetMapping("/list")
	public String viewAccounts(Model model) {
		List<Account> accounts = filterByRole("ROLE_ADMIN", (List<Account>) accountRepo.findAll());
		model.addAttribute("accounts", accounts);
		return "accountList";
	}

	private List<Account> filterByRole(String role, List<Account> accounts) {
		List<Account> list = new ArrayList<>();
		for (Account account : accounts) {
			if (!account.getRoles().equals(role)) {
				list.add(account);
			}
		}
		return list;
	}

	@GetMapping("/disable/{id}")
	public String disableAccount(@PathVariable("id") Long id) {
		Account account = accountRepo.findById(id).orElse(null);
		if (account.isActive()) {
			account.setActive(false);
			accountRepo.save(account);
		}
		return "redirect:/account/list";
	}

	@GetMapping("/enable/{id}")
	public String enableAccount(@PathVariable("id") Long id) {
		Account account = accountRepo.findById(id).orElse(null);
		if (!account.isActive()) {
			account.setActive(true);
			accountRepo.save(account);
		}
		return "redirect:/account/list";
	}

}
