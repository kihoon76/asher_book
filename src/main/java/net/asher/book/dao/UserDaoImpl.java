package net.asher.book.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import net.asher.book.domain.Account;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
	private final static String namespace = "mappers.userMapper";
	
	@Resource(name = "mySqlSession")
	SqlSession mySqlSession;

	@Override
	public Account selectUserInfo(String username) {
		return mySqlSession.selectOne(namespace + ".selectUserInfo", username);
	}
	

}
