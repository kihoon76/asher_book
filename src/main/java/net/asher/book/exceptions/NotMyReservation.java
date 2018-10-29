package net.asher.book.exceptions;


public class NotMyReservation extends RuntimeException {

	public NotMyReservation(String msg) {
		super(msg);
	}
	
	public NotMyReservation(String msg, Throwable t) {
		super(msg, t);
	}

}
