package net.asher.book.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.LogSend;
import net.asher.book.domain.RentalHistory;
import net.asher.book.exceptions.NotMyReservation;
import net.asher.book.service.BookService;
import net.asher.book.service.LogService;
import net.asher.book.service.UserService;
import net.asher.book.util.RestClient;
import net.asher.book.util.SessionUtil;
import net.asher.book.websocket.AsherWebSocketHandler;

@RequestMapping("/user")
@Controller
public class UserController {

	@Resource(name="userService")
	UserService userService;
	
	@Resource(name="asherWebSocketHandler")
	AsherWebSocketHandler asherWebSocketHandler;
	
	@Value("#{smsCfg['key']}")
	String smsKey;
	
	@Value("#{smsCfg['userId']}")
	String smsUserId;
	
	@Value("#{smsCfg['sender']}")
	String smsSender;
	
	@Resource(name="logService")
	LogService logService;
	
	@PostMapping("apply/rental")
	@ResponseBody
	public AjaxVO<RentalHistory> rentalBook(@RequestParam("bookNum") String bookNum) {
		
		AjaxVO<RentalHistory> vo = new AjaxVO<RentalHistory>();
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		try {
			//내가 현재 반납 안된 도서가 있을경우 
			List<RentalHistory> list = userService.getMyNotReturedBooks(memberIdx);
			
			if(list == null || list.size() == 0) {
				//다른사람이 대여한 경우
				RentalHistory r = userService.getUserRentalBookByNum(bookNum); 
				
				if(r == null) {
					Map<String, String> param = new HashMap<>();
					param.put("memberIdx", memberIdx);
					param.put("bookNum", bookNum);
					
					userService.doApplyRental(param); //rental 신청
					vo.setSuccess(true);
					
					Map<String, String> webMsg = new HashMap<>();
					webMsg.put("bookNum", bookNum);
					webMsg.put("memberName", SessionUtil.getSessionAccount().getUserName());
					webMsg.put("memberIdx", memberIdx);
					webMsg.put("type", "R");
					asherWebSocketHandler.sendDatabaseMsg(new Gson().toJson(webMsg));
					
				}
				else {
					vo.setSuccess(false);
					if("A".equals(r.getStatus())) {
						//대여중
						vo.setErrCode("601");
					}
					else {
						//대여신청중
						vo.setErrCode("602");
					}
					
					vo.addObject(r);
				}
			}
			else {
				vo.setSuccess(false);
				vo.setErrCode("600");
				vo.setDatas(list);
			}
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrCode("500");
		}
		
		
		return vo;
		
	}
	
	@PostMapping("cancel/apply")
	@ResponseBody
	public AjaxVO cancelApply(@RequestParam("bookNum") String bookNum) {
		
		return commonCancelApply(bookNum, null);
		
	}
	
	public AjaxVO commonCancelApply(String bookNum, String memberIdx) {
		AjaxVO vo = new AjaxVO();
		
		Map<String, String> param = new HashMap<>();
		param.put("bookNum", bookNum);
		
		if(memberIdx == null) {
			param.put("memberIdx", SessionUtil.getSessionUserIdx());
		}
		else {
			param.put("memberIdx", memberIdx);
		}
	
		try {
			int r = userService.isPossibleApplyCancel(param);
			
			if(r == 1) {
				boolean result = userService.cancelMyApply(param);
				
				if(result) {
					vo.setSuccess(true);
					
					Map<String, String> webMsg = new HashMap<>();
					Map<String, String> info = null;
					
					try {
						//예약으로 자동 신청 상태이면 다음 예약자로 설정
						//예약된 도서인지 확인
						info = userService.applyRentalByReservation(param);
						if(info == null) {
							throw new Exception();
						}
						
						//예약건 처리 완료
						webMsg.put("bookNum", param.get("bookNum"));
						webMsg.put("memberIdx", info.get("memberIdx"));
						webMsg.put("memberName", info.get("memberName"));
						webMsg.put("type", "R");
						
						
					}
					catch(Exception e) {
						webMsg.put("bookNum", param.get("bookNum"));
						webMsg.put("type", "D");
					}
					
					asherWebSocketHandler.sendDatabaseMsg(new Gson().toJson(webMsg));
					
					if("R".equals(webMsg.get("type"))) {
						if(info != null) {
							//문자발송
							StringBuilder sb = new StringBuilder();
							sb.append("receiver=" + info.get("phone").replaceAll("-", ""));
							sb.append("&destination=" + info.get("phone").replaceAll("-", "") + "|" + info.get("memberName"));
							sb.append("&msg=예약하신 책[ " + info.get("bookNum") + "." + info.get("bookName") + "]이 반납되어 대여신청 상태로 변경되었습니다. ");
							sb.append("&title=아셀교회(반납도서)");
							sb.append("&testmode_yn=N");
//							
							RestClient rc = new RestClient(smsKey, smsUserId, smsSender);
							
							String rcr = rc.post("/send/", sb.toString());
							Map<String, String> rcm = new Gson().fromJson(rcr, Map.class);
							
							LogSend log = new LogSend();
							log.setTargetIdx(info.get("memberIdx"));
							log.setTxMsg(sb.toString());
							log.setRxMsg(rcr);
							log.setType("S");
							
							if("1".equals(rcm.get("result_code"))) {
								log.setIsErr("N");
								log.setMsgId(rcm.get("msg_id"));
							}
							else {
								log.setIsErr("Y");
								log.setMsgId("");
							}
							
							List<LogSend> logList = new ArrayList<>();
							logList.add(log);
							
							Map<String, Object> dbParam = new HashMap<>();
							dbParam.put("list",  logList);
							logService.writeLog(dbParam);
						}
					}
				}
				else {
					vo.setSuccess(false);
					vo.setErrCode("603");
				}
			}
			else {
				vo.setSuccess(false);
				vo.setErrCode("603");
			}
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrMsg(e.getMessage());
		}
		
		return vo;
	}
	
	@PostMapping("reserve/cancel")
	@ResponseBody
	public AjaxVO<Map<String, String>> cancelReservation(@RequestParam("reserveBookNum") String reserveBookNum) {
		
		AjaxVO<Map<String, String>> vo = new AjaxVO<>();
		
		Map<String, String> m = new HashMap<>();
		m.put("memberIdx", SessionUtil.getSessionUserIdx());
		m.put("bookNum", reserveBookNum);
		
		try {
			List<Map<String, String>> list = userService.removeMyReservation(m);
			vo.setSuccess(true);
			vo.setDatas(list);
		}
		catch(NotMyReservation e) {
			vo.setSuccess(false);
			vo.setErrCode("607");
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrCode("etc");
			vo.setErrMsg(e.getMessage());
		}
		
		return vo;
	}
	
	
}
