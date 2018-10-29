package net.asher.book.dao;

import java.util.List;
import java.util.Map;

import net.asher.book.domain.Account;
import net.asher.book.domain.Book;
import net.asher.book.domain.RentalHistory;

public interface BookDao {

	List<Book> selectBookList(String memberIdx);

	Book selectBookDetail(String bookNum);

	List<Account> selectReadMemberList(String bookNum);

	List<Book> selectRentaledBookList(String memberIdx);

	int selectRentaledBook(String bookNum);

	int insertReservation(Map<String, String> param);

	Map<String, String> selectPureReservation(String string);


}
