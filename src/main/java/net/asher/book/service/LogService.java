package net.asher.book.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.asher.book.dao.LogDao;

@Service("logService")
public class LogService {
	
	@Resource(name="logDao")
	LogDao logDao;
	
	public void writeLog(Map<String, Object> dbParam) {
		logDao.insertLog(dbParam);
		
	}

}
