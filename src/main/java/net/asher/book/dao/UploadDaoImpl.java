package net.asher.book.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository("uploadDao")
public class UploadDaoImpl implements UploadDao {
	private final static String namespace = "mappers.uploadMapper";
	
	@Resource(name = "mySqlSession")
	SqlSession mySqlSession;
	
	@Override
	public void insertEvent(Map<String, String> param) {
		mySqlSession.insert(namespace + ".insertEvent", param);
	}

}
