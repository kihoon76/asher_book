package net.asher.book.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.asher.book.dao.UploadDao;
import net.asher.book.domain.Event;


@Service("uploadService")
public class UploadService {

	@Value("#{pathCfg['upload']}")
	private String UPLOAD_LOCATION;
	
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

	@Transactional(isolation=Isolation.DEFAULT, 
			   propagation=Propagation.REQUIRED, 
			   rollbackFor=Exception.class,
			   timeout=10)//timeout 초단위
	public void removeEvent(String suffix) {
		Event delEvent = uploadDao.selectEvent(suffix);
		uploadDao.deleteEvent(suffix);
		
		File file = new File(UPLOAD_LOCATION + delEvent.getFileName());
		if(file.exists()) {
			file.delete();
		}
		
	}

	

}
