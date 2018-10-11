package net.asher.book.domain;

import org.apache.ibatis.type.Alias;

@Alias("RentalHistory")
public class RentalHistory extends Book {

	private String rentalDate;
	private String returnDate;		//default rentalDate + 7 day
	private String status;			//R : 대여신청  A: 승인
	
	public String getRentalDate() {
		return rentalDate;
	}
	public void setRentalDate(String rentalDate) {
		this.rentalDate = rentalDate;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
