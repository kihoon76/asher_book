package net.asher.book.dao;

import java.util.List;
import java.util.Map;

import net.asher.book.domain.Account;
import net.asher.book.domain.RentalHistory;

public interface UserDao {

	Account selectUserInfo(String username);

	List<RentalHistory> selectMyNotReturedBooks(String memberIdx);

	RentalHistory selectUserRentalBookByNum(String bookNum);

	int insertApplyRental(Map<String, String> param);

}
