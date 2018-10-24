package net.asher.book.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import net.asher.book.domain.Event;

@Repository("uploadDao")
public class UploadDaoImpl implements UploadDao {
	private final static String namespace = "mappers.uploadMapper";
	
	@Resource(name = "mySqlSession")
	SqlSession mySqlSession;
	
	@Override
	public void insertEvent(Map<String, String> param) {
		mySqlSession.insert(namespace + ".insertEvent", param);
	}

	@Override
	public List<Event> selectEventList(String search) {
		Map<String, String> m = new HashMap<>();
		m.put("search", search);
		return mySqlSession.selectList(namespace + ".selectEventList", m);
	}

	@Override
	public Event selectEvent(String suffix) {
		return mySqlSession.selectOne(namespace + ".selectEvent", suffix);
	}

	@Override
	public void deleteEvent(String suffix) {
		mySqlSession.update(namespace + ".deleteEvent", suffix);
		
	}

}
