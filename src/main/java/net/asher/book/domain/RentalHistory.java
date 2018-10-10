package net.asher.book.domain;

import org.apache.ibatis.type.Alias;

@Alias("RentalHistory")
public class RentalHistory extends Book {

	private String idx;
	private String rentalDate;
	private String returnDate;		//default rentalDate + 7 day
	private String returned;
	private String status;			//R : 대여신청  A: 승인
	private String returnExpired;	//Y: 연체, N: 납기일내 반납
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
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
	public String getReturned() {
		return returned;
	}
	public void setReturned(String returned) {
		this.returned = returned;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReturnExpired() {
		return returnExpired;
	}
	public void setReturnExpired(String returnExpired) {
		this.returnExpired = returnExpired;
	}
	
}
