package net.asher.book.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.asher.book.domain.AjaxVO;
import net.asher.book.service.UserService;
import net.asher.book.util.SessionUtil;
import net.asher.book.websocket.AsherWebSocketHandler;

@RequestMapping("/admin")
@Controller
public class AdminController {
	
	@Resource(name="asherWebSocketHandler")
	AsherWebSocketHandler asherWebSocketHandler;
	
	@Resource(name="userService")
	UserService userService;

	@PostMapping("accept/rental/apply")
	@ResponseBody
	public AjaxVO acceptRentalApply(@RequestBody Map<String, String> param) {
		
		AjaxVO vo = new AjaxVO<>();
		
		try {
			userService.acceptRentalApply(param);
			
			//성공하면 websocket으로 결과 보낸다.
			Map<String, String> webMsg = new HashMap<>();
			webMsg.put("bookNum", param.get("bookNum"));
			webMsg.put("memberName", param.get("memberName"));
			webMsg.put("type", "A");
			asherWebSocketHandler.sendDatabaseMsg(new Gson().toJson(webMsg));
			vo.setSuccess(true);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrMsg(e.getMessage());
		}
	
		return vo;
		
	}
	
	@PostMapping("return/rental")
	@ResponseBody
	public AjaxVO returnRental(@RequestBody Map<String, String> param) {
		
		AjaxVO vo = new AjaxVO<>();
		
		try {
			userService.returnRental(param);
			
			//성공하면 websocket으로 결과 보낸다.
			Map<String, String> webMsg = new HashMap<>();
			webMsg.put("bookNum", param.get("bookNum"));
			webMsg.put("type", "T");
			asherWebSocketHandler.sendDatabaseMsg(new Gson().toJson(webMsg));
			vo.setSuccess(true);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrMsg(e.getMessage());
		}
	
		return vo;
		
	}
	
}
