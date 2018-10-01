package net.asher.book.dao;

import net.asher.book.domain.Account;

public interface UserDao {

	Account selectUserInfo(String username);

}
