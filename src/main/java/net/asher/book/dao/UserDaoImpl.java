package net.asher.book.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import net.asher.book.domain.Account;
import net.asher.book.domain.RentalHistory;
import net.asher.book.domain.Reservation;
import net.asher.book.domain.ReturnHistory;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
	private final static String namespace = "mappers.userMapper";
	
	@Resource(name = "mySqlSession")
	SqlSession mySqlSession;

	@Override
	public Account selectUserInfo(String username) {
		return mySqlSession.selectOne(namespace + ".selectUserInfo", username);
	}

	@Override
	public List<RentalHistory> selectMyNotReturedBooks(String memberIdx) {
		return mySqlSession.selectList(namespace + ".selectMyNotReturedBooks", memberIdx);
	}

	@Override
	public RentalHistory selectUserRentalBookByNum(String bookNum) {
		return mySqlSession.selectOne(namespace + ".selectUserRentalBookByNum", bookNum);
	}

	@Override
	public int insertApplyRental(Map<String, String> param) {
		return mySqlSession.insert(namespace + ".insertApplyRental", param);
	}
	

	@Override
	public List<RentalHistory> selectRentalList(String type) {
		if("R".equals(type)) {
			return mySqlSession.selectList(namespace + ".selectRentalApplyList");
		}
		else {
			return null;
		}
		
	}

	@Override
	public int updateRentalApply(Map<String, String> param) {
		return mySqlSession.update(namespace + ".updateRentalApply", param);
	}
	
	@Override
	public List<ReturnHistory> selectMyRentalHistories(String memberIdx) {
		return mySqlSession.selectList(namespace + ".selectMyRentalHistories", memberIdx);
	}

	@Override
	public int updateReturnRental(Map<String, String> param) {
		return mySqlSession.update(namespace + ".updateReturnRental", param);
	}

	@Override
	public int selectMyApplyBook(Map<String, String> param) {
		return mySqlSession.selectOne(namespace + ".selectMyApplyBook", param);
	}

	@Override
	public int deleteMyApplyBook(Map<String, String> param) {
		return mySqlSession.delete(namespace + ".deleteMyApplyBook", param);
	}

	@Override
	public int insertReturnRental(Map<String, String> param) {
		return mySqlSession.insert(namespace + ".insertReturnRental", param);
	}

	@Override
	public void updateReturnDate(String bookNum) {
		mySqlSession.update(namespace + ".updateReturnDate", bookNum);
		
	}

	@Override
	public List<Map<String, String>> selectExpiredRentals() {
		return mySqlSession.selectList(namespace + ".selectExpiredRentals");
	}

	@Override
	public void insertUser(Map<String, String> param) {
		mySqlSession.insert(namespace + ".insertUser", param);
		
	}

	@Override
	public List<Account> selectUserList() {
		return mySqlSession.selectList(namespace + ".selectUserList");
	}

	@Override
	public List<Map<String, String>> selectReserveMembers(String reserveBookNum) {
		return mySqlSession.selectList(namespace + ".selectReserveMembers", reserveBookNum);
	}

	@Override
	public int selectMyReservation(Map<String, String> param) {
		return mySqlSession.selectOne(namespace + ".selectMyReservation", param);
	}

	@Override
	public Map<String, String> applyRentalByReservation(Map<String, String> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteReservation(Map<String, String> map) {
		return mySqlSession.delete(namespace + ".deleteReservation", map);
		
	}

	@Override
	public List<Reservation> selectMyReservations(String memberIdx) {
		return mySqlSession.selectList(namespace + ".selectMyReservations", memberIdx);
	}
}
