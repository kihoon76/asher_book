package net.asher.book.dao;

import java.util.List;

import net.asher.book.domain.Account;
import net.asher.book.domain.Book;
import net.asher.book.domain.RentalHistory;

public interface BookDao {

	List<Book> selectBookList(String memberIdx);

	Book selectBookDetail(String bookNum);

	List<Account> selectReadMemberList(String bookNum);

	List<Book> selectRentaledBookList();

}
