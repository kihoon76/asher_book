package net.asher.book.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
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
import net.asher.book.domain.RentalHistory;
import net.asher.book.domain.ReturnHistory;
import net.asher.book.service.BookService;
import net.asher.book.service.UserService;
import net.asher.book.util.HttpHeaderUtil;
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
	
	@Resource(name="mailUtil")
	MailUtil mailUtil;
	
	@Value("#{smsCfg['key']}")
	String smsKey;
	
	@Value("#{smsCfg['userId']}")
	String smsUserId;
	
	@Value("#{smsCfg['sender']}")
	String smsSender;
	
	@GetMapping("signin/{errCode}")
	public String signinForm(@PathVariable(name="errCode", required=false) String errCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//ajax call 처리
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			AjaxVO ajax = new AjaxVO();
			if("200".equals(errCode)) {
				ajax.setSuccess(true);
			}
			else {
				ajax.setSuccess(false);
			}
			
			ajax.setErrCode(errCode);
			request.setAttribute("result", ajax);
			
			return "authresult";
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
		
		checkRentalExpire(request);
		return "main";
	}
	
	@GetMapping("info")
	public String info(ModelMap mm) {
		mm.addAttribute("footbar", "info");
		
		
		return "info";
	}
	
	@GetMapping("signin")
	public String getSigninPage() {
		return "signin";
	}
	
	@Scheduled(cron="0 0 9 * * *") //매일  오전 9시
	public void checkRentalExpire(HttpServletRequest request) {
		//schedule.viewDatabaseConnection();
		
		try {
			List<Map<String, String>> list = new ArrayList<>();//userService.getExpiredRentals();
			
			//List<Map<String, String>> list = userService.getExpiredRentals();
			Map<String, String> m = new HashMap<>();
			m.put("email", "lovedeer118@gmail.com");
			m.put("memberName", "남기훈");
			m.put("bookNum", "1");
			m.put("bookName", "테스트");
			m.put("returnDate", "2018-09-09");
			list.add(m);
			
			if(list != null && list.size() > 0) {
				for(int r=0; r<list.size(); r++) {
					Email email = new Email(HttpHeaderUtil.getUrlRoot(request));
					Account account = new Account();
					account.setEmail(list.get(r).get("email"));
					account.setPhone("01032780212");
					account.setUserName("남기훈");
					email.setAccount(account);
					email.setContent("<p>[" + list.get(r).get("memberName")+ "]님이 대여하신 책 <span style=\"color:#ff0000; font-weight:bolder;\">" + list.get(r).get("bookNum") + "." + list.get(r).get("bookName") + "</span> 의 반납일자는 " + list.get(r).get("returnDate") + "입니다.</p>");
					mailUtil.sendMail(email);
					
					RestClient rc = new RestClient(smsKey, smsUserId, smsSender);
					Map<String, String> map = new HashMap<String, String>();
					map.put("receiver", account.getPhone().replaceAll("-", ""));
					map.put("destination", account.getPhone().replaceAll("-", "") + "|" + account.getUserName());
					map.put("msg", "test");
					map.put("title", "test");
					map.put("testmode_yn", "Y");
					String rr = rc.post("/send/", map);
					System.out.println(rr);
				}
	
			}
		} 
		catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
