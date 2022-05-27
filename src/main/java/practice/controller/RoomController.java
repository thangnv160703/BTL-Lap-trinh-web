package practice.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import practice.model.Account;
import practice.model.Room;
import practice.repository.RoomRepository;

@Slf4j
@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private RoomRepository roomRepo;

	@GetMapping
	public String viewList(Model model, HttpSession session) {
		List<Room> rooms = (List<Room>) roomRepo.findAll();
		model.addAttribute("rooms", rooms);
		return "roomList";
	}

	@GetMapping("/details/{id}")
	public String viewDetails(Model model, @PathVariable("id") Long id, HttpSession session) {
		Room room = roomRepo.findById(id).orElse(null);
		model.addAttribute("room", room);
		return "roomDetails";
	}

}
