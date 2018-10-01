package net.asher.book.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.BookDao;
import net.asher.book.domain.Book;

@Service("bookService")
public class BookService {

	@Resource(name="bookDao")
	BookDao bookDao;
	
	public List<Book> getBookList() {
		return bookDao.selectBookList();
	}

}
