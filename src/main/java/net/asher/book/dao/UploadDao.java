package net.asher.book.dao;

import java.util.List;
import java.util.Map;

import net.asher.book.domain.Event;

public interface UploadDao {

	void insertEvent(Map<String, String> param);

	List<Event> selectEventList();

}
