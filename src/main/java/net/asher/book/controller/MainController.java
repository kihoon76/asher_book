package net.asher.book.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import net.asher.book.domain.Account;
import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.Email;
import net.asher.book.domain.LogSend;
import net.asher.book.domain.RentalHistory;
import net.asher.book.domain.ReturnHistory;
import net.asher.book.service.BookService;
import net.asher.book.service.LogService;
import net.asher.book.service.UploadService;
import net.asher.book.service.UserService;
import net.asher.book.util.MailUtil;
import net.asher.book.util.RestClient;
import net.asher.book.util.SessionUtil;

@RequestMapping("/")
@Controller
public class MainController {

	@Resource(name="bookService")
	BookService bookService;
	
	@Resource(name="userService")
	UserService userService;
	
	@Resource(name="logService")
	LogService logService;
	
	@Resource(name="uploadService")
	UploadService uploadService;
	
	@Resource(name="mailUtil")
	MailUtil mailUtil;
	
	@Value("#{smsCfg['key']}")
	String smsKey;
	
	@Value("#{smsCfg['userId']}")
	String smsUserId;
	
	@Value("#{smsCfg['sender']}")
	String smsSender;
	
	@Value("#{pathCfg['contextRoot']}")
	String contextPath;
	
	@GetMapping("signin/{errCode}")
	public String signinForm(@PathVariable(name="errCode", required=false) String errCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//ajax call 처리
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			boolean isCustom = "Y".equals(request.getHeader("CUSTOM")) ? true : false;
			AjaxVO ajax = new AjaxVO();
			if("200".equals(errCode)) {
				ajax.setSuccess(true);
			}
			else {
				ajax.setSuccess(false);
			}
			
			ajax.setErrCode(errCode);
			request.setAttribute("result", ajax);
			
			//return "authresult";
			return isCustom ? "authresult" : "errorPage";
		}
		
		response.sendRedirect("/signin");
		return null;
		
	}
	
	@GetMapping("main")
	public String main(ModelMap mm, HttpServletRequest request) {
		Account account = SessionUtil.getSessionAccount();
		
		mm.addAttribute("bookList", bookService.getBookList(account.getIdx()));
		mm.addAttribute("memberName", account.getUserName());
		mm.addAttribute("footbar", "home");
		
		List<RentalHistory> myRentalList = userService.getMyNotReturedBooks(account.getIdx());
		List<ReturnHistory> myRentalHistoryList = userService.getMyRentalHistories(account.getIdx());
		
		
		if(myRentalList != null && myRentalList.size() > 0) {
			mm.addAttribute("myRentalList", myRentalList);
		}
		
		if(myRentalHistoryList != null && myRentalHistoryList.size() > 0) {
			mm.addAttribute("myRentalHistoryList", myRentalHistoryList);
		}
		
		//checkRentalExpire(request);
		return "main";
	}
	
	@GetMapping("info")
	public String info(ModelMap mm) {
		Account account = SessionUtil.getSessionAccount();
		mm.addAttribute("footbar", "info");
		mm.addAttribute("bookList", bookService.getBookList(account.getIdx()));
		
		return "info";
	}
	
	@GetMapping("signin")
	public String getSigninPage() {
		return "signin";
	}
	
	/*
	 * Using @Scheduled annotation would in turn make Spring container understand that the method underneath this annotation would run as a job.
	 * Remember that the methods annotated with @Scheduled should not have parameters passed to them.
	 * They should not return any values too.
	 * If you want the external objects to be used within your @Scheduled methods, you should inject them into the DemoService class using autowiring rather than passing them as parameters to the @Scheduled methods.
	 * */
	
	//@Scheduled(cron="0 0/1 * * * *")
	@Scheduled(cron="0 0 9 * * *") //매일  오전 9시
	public void checkRentalExpire() {
		
		try {
			
			List<Map<String, String>> list = userService.getExpiredRentals();
			
			if(list != null && list.size() > 0) {
				int cnt = list.size();
				ExecutorService excutorService = Executors.newFixedThreadPool(cnt);
				
				for(int r=0; r<cnt; r++) {
					class R implements Runnable {
						Map<String, String> map;
						
						R(Map<String, String> m) {
							map = m;
						}
						
						@Override
						public void run() {
							Email email = new Email(contextPath);
							String msg = "<p>[" + map.get("memberName")+ "]님이 대여하신 책 <span style=\"color:#ff0000; font-weight:bolder;\">" + map.get("bookNum") + "." + map.get("bookName") + "</span> 의 반납일자는 " + map.get("returnDate") + "입니다.</p>";
							Account account = new Account();
							account.setIdx(map.get("memberIdx"));
							account.setEmail(map.get("email"));
							account.setPhone(map.get("phone"));
							account.setUserName(map.get("memberName"));
							email.setAccount(account);
							email.setContent(msg);
							
							LogSend log = new LogSend();
							log.setTargetIdx(map.get("memberIdx"));
							log.setTxMsg(msg);
							log.setType("E");
							log.setMsgId("");
							try {
								mailUtil.sendMail(email);
								log.setIsErr("N");
								log.setRxMsg("");
							} 
							catch (AddressException e) {
								log.setIsErr("Y");
								log.setRxMsg(e.getMessage());
							}
							catch (MessagingException e) {
								log.setIsErr("Y");
								log.setRxMsg(e.getMessage());
							}
							
							RestClient rc = new RestClient(smsKey, smsUserId, smsSender);
							
							StringBuilder sb = new StringBuilder();
							sb.append("receiver=" + account.getPhone().replaceAll("-", ""));
							sb.append("&destination=" + account.getPhone().replaceAll("-", "") + "|" + account.getUserName());
							sb.append("&msg=대여하신 책[" + map.get("bookNum") + "." + map.get("bookName") + "]의 반납일자는 " + map.get("returnDate") + "입니다.");
							sb.append("&title=아셀교회");
							sb.append("&testmode_yn=N");
							
							String rcr = rc.post("/send/", sb.toString());
							Map<String, String> rcm = new Gson().fromJson(rcr, Map.class);
							
							LogSend log2 = new LogSend();
							log2.setTargetIdx(map.get("memberIdx"));
							log2.setTxMsg(sb.toString());
							log2.setRxMsg(rcr);
							log2.setType("S");
							
							if("1".equals(rcm.get("result_code"))) {
								log2.setIsErr("N");
								log2.setMsgId(rcm.get("msg_id"));
							}
							else {
								log2.setIsErr("Y");
								log2.setMsgId("");
							}
							
							List<LogSend> logList = new ArrayList<>();
							logList.add(log);
							logList.add(log2);
							
							Map<String, Object> dbParam = new HashMap<>();
							dbParam.put("list",  logList);
							logService.writeLog(dbParam);
						}
					};
					
					excutorService.execute(new R(list.get(r)));
					
				}
				
				excutorService.shutdown();
	
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("forbidden")
	public String viewForbidden() {
		
		return "accessError";
	}
	
	@GetMapping("link/item/{suffix}")
	public String getEventDetail(
			@PathVariable("suffix") String suffix,
			ModelMap mm) {
		mm.addAttribute("event", uploadService.getEventDetail(suffix));
		
		return "event/eventTemplate";
	}
	
}
