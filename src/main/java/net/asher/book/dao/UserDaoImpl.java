package net.asher.book.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import net.asher.book.domain.Account;
import net.asher.book.domain.RentalHistory;

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
	

}
