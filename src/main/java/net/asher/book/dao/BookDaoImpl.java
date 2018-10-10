package net.asher.book.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import net.asher.book.domain.Book;
import net.asher.book.domain.RentalHistory;

@Repository("bookDao")
public class BookDaoImpl implements BookDao {

	private final static String namespace = "mappers.bookMapper";
	
	@Resource(name = "mySqlSession")
	SqlSession mySqlSession;

	@Override
	public List<Book> selectBookList(String memberIdx) {
		return mySqlSession.selectList(namespace + ".selectBookList", memberIdx);
	}

}
