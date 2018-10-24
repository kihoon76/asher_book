package net.asher.book.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.asher.book.domain.AjaxVO;
import net.asher.book.service.UploadService;
import net.asher.book.util.SessionUtil;

@RequestMapping("/upload")
@Controller
public class UploadController {

	@Value("#{pathCfg['upload']}")
	private String UPLOAD_LOCATION;
	
	@Resource(name="uploadService")
	UploadService uploadService;
	
	@PostMapping("event/regWithPic")
	@ResponseBody
	public AjaxVO uploadRegEvent(
			@RequestParam("eventImage") MultipartFile[] files,
			@RequestParam("eventTitle") String eventTitle,
			@RequestParam("eventContent") String eventContent) {
		
		AjaxVO vo = new AjaxVO();
		
		vo.setSuccess(true);
		
		System.err.println("eventTitle====>"+eventTitle);
		System.err.println("eventContent====>"+eventContent);
		
		List<String> fileNames = new ArrayList<String>();
		String fileName = null;
		long fileSize = 0;
		try {
			
			String nanoTime = String.valueOf(System.nanoTime());
			
			if(files.length > 0) {
				fileName = "event" + nanoTime + "." + FilenameUtils.getExtension(files[0].getOriginalFilename());
				FileCopyUtils.copy(files[0].getBytes(), new File(UPLOAD_LOCATION + fileName));
				fileSize = files[0].getSize();
			}
			else {
				fileName = "eventNoImage.png";
			}
			
			Map<String, String> param = new HashMap<>();
			param.put("eventTitle", eventTitle);
			param.put("eventContent", eventContent);
			param.put("fileName", fileName);
			param.put("suffix", nanoTime);
			param.put("writer", SessionUtil.getSessionUserIdx());
			param.put("fileSize", String.valueOf(fileSize));
			uploadService.regEvent(param);
			
			
		}
		catch(Exception e) {
			File file = new File(UPLOAD_LOCATION + fileName);
			if(file.exists()) {
				file.delete();
			}

			vo.setSuccess(false);
			vo.setErrCode("etc");
			vo.setErrMsg(e.getMessage());
		}
		
		return vo;
	}
	
	@PostMapping("event/regWithNoPic")
	@ResponseBody
	public AjaxVO regEvent(@RequestBody Map<String, String> param) {
		
		AjaxVO vo = new AjaxVO();
		
		String nanoTime = String.valueOf(System.nanoTime());
		
		param.put("fileName", "eventNoImage.png");
		param.put("suffix", nanoTime);
		param.put("writer", SessionUtil.getSessionUserIdx());
		param.put("fileSize", "0");
		
		try {
			uploadService.regEvent(param);
			vo.setSuccess(true);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrCode("etc");
			vo.setErrMsg(e.getMessage());
		}
		
		return vo;
	}
	
}
