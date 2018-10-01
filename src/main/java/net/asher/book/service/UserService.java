package net.asher.book.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.UserDao;
import net.asher.book.domain.Account;

@Service("userService")
public class UserService {

	@Resource(name="userDao")
	UserDao userDao;
	
	public Account getUserInfo(String username) {
		return userDao.selectUserInfo(username);
	}

}
