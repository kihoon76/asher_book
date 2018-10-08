package net.asher.book.dao;

import java.util.List;

import net.asher.book.domain.Book;

public interface BookDao {

	List<Book> selectBookList(String memberIdx);

}
