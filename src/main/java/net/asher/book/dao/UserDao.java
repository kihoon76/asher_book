package net.asher.book.dao;

import java.util.List;
import java.util.Map;

import net.asher.book.domain.Account;
import net.asher.book.domain.RentalHistory;
import net.asher.book.domain.Reservation;
import net.asher.book.domain.ReturnHistory;

public interface UserDao {

	Account selectUserInfo(String username);

	List<RentalHistory> selectMyNotReturedBooks(String memberIdx);

	RentalHistory selectUserRentalBookByNum(String bookNum);

	int insertApplyRental(Map<String, String> param);

	List<RentalHistory> selectRentalList(String type);

	int updateRentalApply(Map<String, String> param);
	
	public List<ReturnHistory> selectMyRentalHistories(String memberIdx);

	int updateReturnRental(Map<String, String> param);

	int selectMyApplyBook(Map<String, String> param);

	int deleteMyApplyBook(Map<String, String> param);

	int insertReturnRental(Map<String, String> param);

	void updateReturnDate(String bookNum);

	List<Map<String, String>> selectExpiredRentals();

	void insertUser(Map<String, String> param);

	List<Account> selectUserList();

	List<Map<String, String>> selectReserveMembers(String reserveBookNum);

	int selectMyReservation(Map<String, String> param);

	Map<String, String> applyRentalByReservation(Map<String, String> param);

	int deleteReservation(Map<String, String> map);

	List<Reservation> selectMyReservations(String idx);

}
