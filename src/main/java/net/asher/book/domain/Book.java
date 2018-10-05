package net.asher.book.domain;

import org.apache.ibatis.type.Alias;

@Alias("Book")
public class Book {

	private int bookNum;
	private String bookName;
	private String rentalPossible;
	private String rentalMan;
	
	public int getBookNum() {
		return bookNum;
	}
	public void setBookNum(int bookNum) {
		this.bookNum = bookNum;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getRentalPossible() {
		return rentalPossible;
	}
	public void setRentalPossible(String rentalPossible) {
		this.rentalPossible = rentalPossible;
	}
	public String getRentalMan() {
		return rentalMan;
	}
	public void setRentalMan(String rentalMan) {
		this.rentalMan = rentalMan;
	}
	
	
}
