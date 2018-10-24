package net.asher.book.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.UploadDao;
import net.asher.book.domain.Event;


@Service("uploadService")
public class UploadService {

	@Resource(name="uploadDao")
	UploadDao uploadDao;
	
	public void regEvent(Map<String, String> param) {
		uploadDao.insertEvent(param);
		
	}

	public List<Event> getEventList() {
		return uploadDao.selectEventList();
	}

	public Event getEventDetail(String suffix) {
		return uploadDao.selectEvent(suffix);
	}

	

}
