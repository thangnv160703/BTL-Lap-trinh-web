package practice.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import practice.model.Booking;
import practice.model.Client;
import practice.model.Room;
import practice.model.Account;
import practice.repository.BookingRepository;
import practice.repository.ClientRepository;
import practice.repository.RoomRepository;
import practice.repository.UserRepository;

@Slf4j
@Controller
@RequestMapping("/booking")
@SessionAttributes("currentRoom")
public class BookingController {

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private BookingRepository bookingRepo;

	@ModelAttribute("currentRoom")
	public Room room() {
		return new Room();
	}

	@GetMapping("/{id}")
	public String bookingForm(Model model, @PathVariable("id") Long id, @ModelAttribute("currentRoom") Room room) {
		Booking booking = new Booking();
		Room room2 = roomRepo.findById(id).orElse(null);
		if (room2 != null) {
			room.setId(room2.getId());
			room.setName(room2.getName());
			room.setPrice(room2.getPrice());
			room.setType(room2.getType());
			room.setDescription(room2.getDescription());
		}
		model.addAttribute("room", room2);
		model.addAttribute("booking", booking);
		return "bookingInfo";
	}

	@PostMapping
	public String createBooking(Model model, Booking currentBooking, HttpSession session) throws ParseException {
		Room room = (Room) session.getAttribute("currentRoom");
		Account account = (Account) session.getAttribute("currentAccount");
		SimpleDateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd");
		Date chin = fomatter.parse(currentBooking.getCheckin());
		Date chout = fomatter.parse(currentBooking.getCheckout());
		if (chin.after(chout) || chin.equals(chout)) {
			model.addAttribute("message", "Kiem tra lai thoi gian");
			model.addAttribute("room", room);
			return "bookingInfo";
		}
		Client client = clientRepo.findByUser(account.getUser()).orElse(null);
		currentBooking.setRoom(room);
		currentBooking.setClient(client);
		currentBooking.setReceive(false);
		currentBooking.setCancelled(false);
		currentBooking.setPaid(false);
		bookingRepo.save(currentBooking);
		return "redirect:/room";
	}

	@GetMapping("/list")
	public String viewBookingList(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("currentAccount");
		Client client = clientRepo.findByUser(account.getUser()).orElse(null);
		List<Booking> bookings = filterByCancel(bookingRepo.findAllByClient(client));
		model.addAttribute("bookings", bookings);
		return "bookingList";
	}

	private List<Booking> filterByCancel(List<Booking> bookings) {
		List<Booking> list = new ArrayList<>();
		for (Booking booking : bookings) {
			if (booking.isCancelled() == false) {
				list.add(booking);
			}
		}
		return list;
	}

	@GetMapping("/cancel/{id}")
	public String cancelBooking(@PathVariable("id") Long id) {
		Booking booking = bookingRepo.findById(id).orElse(null);
		if (booking != null) {
			booking.setCancelled(true);
			bookingRepo.save(booking);
		}
		return "redirect:/booking/list";
	}
}
