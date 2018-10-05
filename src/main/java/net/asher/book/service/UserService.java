package net.asher.book.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.UserDao;
import net.asher.book.domain.Account;
import net.asher.book.domain.RentalHistory;

@Service("userService")
public class UserService {

	@Resource(name="userDao")
	UserDao userDao;
	
	public Account getUserInfo(String username) {
		return userDao.selectUserInfo(username);
	}

	public List<RentalHistory> getMyNotReturedBooks(String memberIdx) {
		return userDao.selectMyNotReturedBooks(memberIdx);
	}

	public RentalHistory getUserRentalBookByNum(String bookNum) {
		return userDao.selectUserRentalBookByNum(bookNum);
	}

}
