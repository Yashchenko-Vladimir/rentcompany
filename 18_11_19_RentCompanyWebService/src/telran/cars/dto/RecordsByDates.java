package telran.cars.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class RecordsByDates implements Serializable {
	LocalDate from;
	LocalDate to;
	
	public RecordsByDates(LocalDate from, LocalDate to) {
		this.from = from;
		this.to = to;
	}
	
	public RecordsByDates() {}
	
	public LocalDate getFrom() {
		return from;
	}
	public LocalDate getTo() {
		return to;
	}
	
}
