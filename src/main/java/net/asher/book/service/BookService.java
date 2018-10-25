package net.asher.book.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.BookDao;
import net.asher.book.domain.Account;
import net.asher.book.domain.Book;
import net.asher.book.domain.RentalHistory;

@Service("bookService")
public class BookService {

	@Resource(name="bookDao")
	BookDao bookDao;
	
	public List<Book> getBookList(String memberIdx) {
		return bookDao.selectBookList(memberIdx);
	}

	public Book getBookDetail(String bookNum) {
		return bookDao.selectBookDetail(bookNum);
	}

	public List<Account> getReadMemberList(String bookNum) {
		return bookDao.selectReadMemberList(bookNum);
	}

	public List<Book> getRentaledBookList() {
		return bookDao.selectRentaledBookList();
	}
}
