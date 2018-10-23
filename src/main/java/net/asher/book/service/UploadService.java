package net.asher.book.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.UploadDao;


@Service("uploadService")
public class UploadService {

	@Resource(name="uploadDao")
	UploadDao uploadDao;
	
	public void regEvent(Map<String, String> param) {
		uploadDao.insertEvent(param);
		
	}

	

}
