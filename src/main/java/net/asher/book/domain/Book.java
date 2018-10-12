package net.asher.book.domain;

import org.apache.ibatis.type.Alias;

@Alias("Book")
public class Book {

	private int bookNum;
	private String bookName;
	private String bookEngName;
	private String rentalPossible;
	private String rentalMan;
	private String rentalManIdx;
	private String mine;
	private String bookContents;
	private String bookLink;
	
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
	public String getRentalManIdx() {
		return rentalManIdx;
	}
	public void setRentalManIdx(String rentalManIdx) {
		this.rentalManIdx = rentalManIdx;
	}
	public String getMine() {
		return mine;
	}
	public void setMine(String mine) {
		this.mine = mine;
	}
	public String getBookEngName() {
		return bookEngName;
	}
	public void setBookEngName(String bookEngName) {
		this.bookEngName = bookEngName;
	}
	public String getBookContents() {
		return bookContents;
	}
	public void setBookContents(String bookContents) {
		this.bookContents = bookContents;
	}
	public String getBookLink() {
		return bookLink;
	}
	public void setBookLink(String bookLink) {
		this.bookLink = bookLink;
	}
	
}
