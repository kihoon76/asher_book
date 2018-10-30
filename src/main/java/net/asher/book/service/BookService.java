package net.asher.book.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.BookDao;
import net.asher.book.dao.UserDao;
import net.asher.book.domain.Account;
import net.asher.book.domain.Book;
import net.asher.book.domain.RentalHistory;

@Service("bookService")
public class BookService {

	@Resource(name="bookDao")
	BookDao bookDao;
	
	@Resource(name="userDao")
	UserDao userDao;
	
	public List<Book> getBookList(String memberIdx) {
		return bookDao.selectBookList(memberIdx);
	}

	public Book getBookDetail(String bookNum) {
		return bookDao.selectBookDetail(bookNum);
	}

	public List<Account> getReadMemberList(String bookNum) {
		return bookDao.selectReadMemberList(bookNum);
	}

	public List<Book> getRentaledBookList(String memberIdx) {
		return bookDao.selectRentaledBookList(memberIdx);
	}

	public boolean isRentaledBook(String bookNum) {
		int r = bookDao.selectRentaledBook(bookNum);
		return r == 1;
	}

	public List<Map<String, String>> regReservation(Map<String, String> param) throws Exception {
		int r = bookDao.insertReservation(param);
		
		if(r == 1) {
			return userDao.selectReserveMembers(param.get("bookNum"));
		}
		
		throw new Exception();
	}

	public List<Map<String, Object>> getStatisticsReadBook() {
		return bookDao.selectStatisticsReadBook();
	}

}
