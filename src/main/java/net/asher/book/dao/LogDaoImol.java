package net.asher.book.dao;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository("logDao")
public class LogDaoImol implements LogDao {
	private final static String namespace = "mappers.logMapper";
	
	@Resource(name = "mySqlSession")
	SqlSession mySqlSession;
	
	@Override
	public void insertLog(Map<String, Object> dbParam) {
		mySqlSession.insert(namespace + ".insertLog", dbParam);
		
	}

}
