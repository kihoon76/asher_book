package net.asher.book.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import net.asher.book.domain.Book;

@Repository("bookDao")
public class BookDaoImpl implements BookDao {

	private final static String namespace = "mappers.bookMapper";
	
	@Resource(name = "mySqlSession")
	SqlSession mySqlSession;

	@Override
	public List<Book> selectBookList() {
		return mySqlSession.selectList(namespace + ".selectBookList");
	}
}
