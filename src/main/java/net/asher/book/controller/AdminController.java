package net.asher.book.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.asher.book.domain.Account;
import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.Event;
import net.asher.book.domain.LogSend;
import net.asher.book.domain.RentalHistory;
import net.asher.book.service.BookService;
import net.asher.book.service.UploadService;
import net.asher.book.service.UserService;
import net.asher.book.util.RestClient;
import net.asher.book.util.SessionUtil;
import net.asher.book.websocket.AsherWebSocketHandler;

@RequestMapping("/admin")
@Controller
public class AdminController {
	
	@Resource(name="asherWebSocketHandler")
	AsherWebSocketHandler asherWebSocketHandler;
	
	@Resource(name="userService")
	UserService userService;
	
	@Resource(name="bookService")
	BookService bookService;
	
	@Resource(name="uploadService")
	UploadService uploadService;
	
	@Resource(name="userController")
	UserController userController;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("#{smsCfg['key']}")
	String smsKey;
	
	@Value("#{smsCfg['userId']}")
	String smsUserId;
	
	@Value("#{smsCfg['sender']}")
	String smsSender;

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
			vo.setSuccess(true);
			try {
				//예약된 도서인지 확인
				Map<String, String> info = userService.applyRentalByReservation(param);
				if(info == null) {
					throw new Exception();
				}
				
				//예약건 처리 완료
				Map<String, String> webMsg = new HashMap<>();
				webMsg.put("bookNum", param.get("bookNum"));
				webMsg.put("memberIdx", info.get("memberIdx"));
				webMsg.put("memberName", info.get("memberName"));
				webMsg.put("type", "R");
				
//				asherWebSocketHandler.sendDatabaseMsg(new Gson().toJson(webMsg));
//				
//				StringBuilder sb = new StringBuilder();
//				sb.append("receiver=" + account.getPhone().replaceAll("-", ""));
//				sb.append("&destination=" + account.getPhone().replaceAll("-", "") + "|" + account.getUserName());
//				sb.append("&msg=대여하신 책 " + map.get("bookNum") + "." + map.get("bookName") + "반납일자는 " + map.get("returnDate") + "입니다.");
//				sb.append("&title=아셀교회");
//				sb.append("&testmode_yn=N");
//				
//				String rcr = rc.post("/send/", sb.toString());
//				Map<String, String> rcm = new Gson().fromJson(rcr, Map.class);
//				
//				LogSend log2 = new LogSend();
//				log2.setTargetIdx(map.get("memberIdx"));
//				log2.setTxMsg(sb.toString());
//				log2.setRxMsg(rcr);
//				log2.setType("S");
//				
//				if("1".equals(rcm.get("result_code"))) {
//					log2.setIsErr("N");
//					log2.setMsgId(rcm.get("msg_id"));
//				}
//				else {
//					log2.setIsErr("Y");
//					log2.setMsgId("");
//				}
//				
//				List<LogSend> logList = new ArrayList<>();
//				logList.add(log);
//				logList.add(log2);
//				
//				Map<String, Object> dbParam = new HashMap<>();
//				dbParam.put("list",  logList);
//				logService.writeLog(dbParam);
			}
			catch(Exception e) {
				//예약건 처리 오류시 반납건 처리는 수행
				Map<String, String> webMsg = new HashMap<>();
				webMsg.put("bookNum", param.get("bookNum"));
				webMsg.put("type", "T");
				asherWebSocketHandler.sendDatabaseMsg(new Gson().toJson(webMsg));
				
			}
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrMsg(e.getMessage());
		}
	
		return vo;
		
	}
	
	@PostMapping("extension/return")
	@ResponseBody
	public AjaxVO extendReturn(@RequestParam("bookNum") String bookNum) {
		
		AjaxVO vo = new AjaxVO<>();
		
		try {
			userService.extendReturn(bookNum);
			vo.setSuccess(true);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrMsg(e.getMessage());
		}
	
		return vo;
		
	}
	
	@GetMapping("reg/user_form")
	public String getRegUserForm(ModelMap mm) {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		mm.addAttribute("bookList", bookService.getBookList(memberIdx));
		
		return "admin/regUserForm";
	}
	
	@GetMapping("list/user")
	public String getUserList(ModelMap mm) {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		List<Account> userList = userService.getUserList();
		
		mm.addAttribute("bookList", bookService.getBookList(memberIdx));
		mm.addAttribute("userList", userList);
		
		return "admin/userList";
	}
	
	@PostMapping("reg/user")
	@ResponseBody
	public AjaxVO regUser(@RequestBody Map<String, String> param) {
		AjaxVO vo = new AjaxVO<>();
		
		String phone = param.get("userPhone");
		phone = phone.replaceAll("-", "");
		param.put("password", passwordEncoder.encode(phone));
		
		try {
			userService.regUser(param);
			vo.setSuccess(true);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrCode("604");
			vo.setErrMsg(e.getMessage());
		}
		
		return vo;
		
	}
	
	@GetMapping("sms/remain")
	public String remainSms(ModelMap mm) {
		
		RestClient rc = new RestClient(smsKey, smsUserId, smsSender);
		
		StringBuilder sb = new StringBuilder();
		sb.append("key=" + smsKey);
		sb.append("&user_id=" + smsUserId);
		
		String rcr = rc.post("/remain/", sb.toString());
		Map<String, String> rcm = new Gson().fromJson(rcr, Map.class);
		
		ObjectMapper om = new ObjectMapper();
		try {
			System.err.println(om.writeValueAsString(rcm));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mm.addAttribute("bookList", bookService.getBookList(SessionUtil.getSessionUserIdx()));
		mm.addAttribute("info", rcm);
		return "admin/remainSms";
	}
	
	@PostMapping("cancel/apply")
	@ResponseBody
	public AjaxVO cancelApply(
			@RequestParam("bookNum") String bookNum,
			@RequestParam("memberIdx") String memberIdx) {
		
		return userController.commonCancelApply(bookNum, memberIdx);
	}
	
	@GetMapping("event/reg_form")
	public String eventRegForm(ModelMap mm) {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		mm.addAttribute("bookList", bookService.getBookList(memberIdx));
		mm.addAttribute("footbar", "event");
		return "event/eventRegForm";
		
	}
	
	@GetMapping("event/list")
	public String getEventList(
			@RequestParam(name="search", required=false) String search,
			ModelMap mm) {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		mm.addAttribute("bookList", bookService.getBookList(memberIdx));
		mm.addAttribute("eventList", uploadService.getEventList(search));
		mm.addAttribute("search", search);
		return "event/eventList";
		
	}
	
	@GetMapping("event/item/{suffix}")
	public String getEventDetail(
			@PathVariable("suffix") String suffix,
			ModelMap mm) {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		mm.addAttribute("bookList", bookService.getBookList(memberIdx));
		mm.addAttribute("event", uploadService.getEventDetail(suffix));
		mm.addAttribute("back", "Y");
		return "event/adminEventTemplate";
	}
	
	@PostMapping("event/delete")
	@ResponseBody
	public AjaxVO deleteEvent(@RequestParam("suffix") String suffix) {
		
		AjaxVO vo = new AjaxVO<>();
		
		try {
			uploadService.removeEvent(suffix);
			vo.setSuccess(true);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrCode("etc");
		}
		
		
		return vo;
	}
}
