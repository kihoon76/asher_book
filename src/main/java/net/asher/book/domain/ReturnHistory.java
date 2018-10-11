package net.asher.book.domain;

import org.apache.ibatis.type.Alias;

@Alias("ReturnHistory")
public class ReturnHistory extends Book {

	private String idx;
	private String rentalDate;
	private String returnDate;
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
	public String getReturnExpired() {
		return returnExpired;
	}
	public void setReturnExpired(String returnExpired) {
		this.returnExpired = returnExpired;
	}
}
