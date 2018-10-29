package net.asher.book.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.asher.book.dao.BookDao;
import net.asher.book.dao.UserDao;
import net.asher.book.domain.Account;
import net.asher.book.domain.RentalHistory;
import net.asher.book.domain.Reservation;
import net.asher.book.domain.ReturnHistory;
import net.asher.book.exceptions.NotMyReservation;

@Service("userService")
public class UserService {

	@Resource(name="userDao")
	UserDao userDao;
	
	@Resource(name="bookDao")
	BookDao bookDao;
	
	public Account getUserInfo(String username) {
		return userDao.selectUserInfo(username);
	}

	public List<RentalHistory> getMyNotReturedBooks(String memberIdx) {
		return userDao.selectMyNotReturedBooks(memberIdx);
	}

	public RentalHistory getUserRentalBookByNum(String bookNum) {
		return userDao.selectUserRentalBookByNum(bookNum);
	}

	public void doApplyRental(Map<String, String> param) {
		userDao.insertApplyRental(param);
	}
	
	public List<RentalHistory> getRentalList(String type) {
		return userDao.selectRentalList(type);
	}

	@Transactional(isolation=Isolation.DEFAULT, 
			   propagation=Propagation.REQUIRED, 
			   rollbackFor=Exception.class,
			   timeout=10)//timeout 초단위
	public int acceptRentalApply(Map<String, String> param) throws Exception {
		
		int r = userDao.updateRentalApply(param);
		
		if(r == 1) {
			return r;
		}
		
		throw new Exception();
	}
	
	public List<ReturnHistory> getMyRentalHistories(String memberIdx) {
		return userDao.selectMyRentalHistories(memberIdx);
	}

	@Transactional(isolation=Isolation.DEFAULT, 
			   propagation=Propagation.REQUIRED, 
			   rollbackFor=Exception.class,
			   timeout=10)//timeout 초단위
	public int returnRental(Map<String, String> param) throws Exception {
		
		int delRowCnt = userDao.selectMyApplyBook(param);
		
		if(delRowCnt != 1) throw new Exception();
		
		int r = userDao.insertReturnRental(param);
		
		if(r == 1) {
			r = userDao.deleteMyApplyBook(param);
			
			if(r == 1) return r;
			throw new Exception();
		}
		
		throw new Exception();
	}

	public int isPossibleApplyCancel(Map<String, String> param) {
		return userDao.selectMyApplyBook(param);
	}

	public boolean cancelMyApply(Map<String, String> param) {
		int r = userDao.deleteMyApplyBook(param);
		
		if(r == 1) return true;
		
		return false;
	}
	
	public List<Map<String, String>> getReserveMembers(String reserveBookNum) {
		return userDao.selectReserveMembers(reserveBookNum);
	}

	public void extendReturn(String bookNum) {
		userDao.updateReturnDate(bookNum);
		
	}

	public List<Map<String, String>> getExpiredRentals() {
		return userDao.selectExpiredRentals();
	}

	public void regUser(Map<String, String> param) {
		 userDao.insertUser(param);
	}

	public List<Account> getUserList() {
		return userDao.selectUserList();
	}

	public boolean isAlreadyReservation(Map<String, String> param) {
		return 1 == userDao.selectMyReservation(param);
	}

	@Transactional(isolation=Isolation.DEFAULT, 
			   propagation=Propagation.REQUIRED, 
			   rollbackFor=Exception.class,
			   timeout=10)//timeout 초단위
	public Map<String, String> applyRentalByReservation(Map<String, String> param/*대여반납자 정보*/) {
		Map<String, String> map = bookDao.selectPureReservation(param.get("bookNum"));
		
		if(map != null) {
			int r = userDao.deleteReservation(map); //예약 테이블에서 삭제
			if(r != 1) throw new RuntimeException();
			
			r = userDao.insertApplyRental(map);
		
			if(r == 1) return map;
			
			throw new RuntimeException();
		}
		
		return null;
		
	}

	public List<Reservation> getMyReservations(String idx) {
		return userDao.selectMyReservations(idx);
	}

	public List<Map<String, String>> removeMyReservation(Map<String, String> m) {
		int r = userDao.deleteReservation(m);
		
		if(r == 1) {
			return userDao.selectReserveMembers(m.get("bookNum"));
		}
		
		throw new NotMyReservation("");
	}

}
