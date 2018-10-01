package net.asher.book.domain;

import org.apache.ibatis.type.Alias;

@Alias("Book")
public class Book {

	private int bookNum;
	private String bookName;
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
	
	
}
