package practice.controller;

import java.util.List;

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
import practice.model.Room;
import practice.repository.RoomRepository;

@Slf4j
@Controller
@RequestMapping("/manage/room")
@SessionAttributes("alteredRoom")
public class ManageRoomController {

	@Autowired
	private RoomRepository roomRepo;

	@ModelAttribute("alteredRoom")
	public Room room() {
		return new Room();
	}

	@GetMapping
	public String manageRoomFrm(Model model) {
		List<Room> rooms = (List<Room>) roomRepo.findAll();
		model.addAttribute("rooms", rooms);
		return "manageRoomList";
	}
	
	@GetMapping("/details/{id}")
	public String manageRoomDetails(Model model, @PathVariable("id") Long id) {
		Room room = roomRepo.findById(id).orElse(null);
		model.addAttribute("room", room);
		return "manageRoomDetails";
	}

	@GetMapping("/change/{id}")
	public String changeRoomInfo(Model model, @PathVariable("id") Long id,
			@SessionAttribute("alteredRoom") Room alteredroom) {
		Room room = roomRepo.findById(id).orElse(null);
		if (room != null) {
			alteredroom.setId(room.getId());
			alteredroom.setName(room.getName());
		}
		model.addAttribute("room", room);
		return "changeRoomDetails";
	}

	@PostMapping("/change")
	public String confirmChange(Room room, @SessionAttribute("alteredRoom") Room alteredroom) {
		alteredroom.setPrice(room.getPrice());
		alteredroom.setType(room.getType());
		alteredroom.setDescription(room.getDescription());
		roomRepo.save(alteredroom);
		return "redirect:/manage/room";
	}
	
	@GetMapping("/add")
	public String addRoom(Model model) {
		model.addAttribute("room", new Room());
		return "manageAddRoom";
	}
	
	@PostMapping("/add")
	public String saveRoom(Room room) {
//		log.info("Room: " + room);
		roomRepo.save(room);
		return "redirect:/manage/room";
	}
}
