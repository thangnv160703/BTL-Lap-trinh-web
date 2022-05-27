package practice.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Bill")
public class Bill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = Booking.class, cascade = CascadeType.MERGE)
	private Booking booking;
	
	@ManyToOne(targetEntity = Account.class, cascade = CascadeType.MERGE)
	private Account receptionist;
	
	private Date paymentDate;
	private Long paymentAmount;
	private String paymentType;
	
	@PrePersist
	private void paidAt() {
		this.paymentDate = new Date();
	}
}
