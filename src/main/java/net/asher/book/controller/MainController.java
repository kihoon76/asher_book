package net.asher.book.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.asher.book.domain.Account;
import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.RentalHistory;
import net.asher.book.service.BookService;
import net.asher.book.service.UserService;
import net.asher.book.util.SessionUtil;

@RequestMapping("/")
@Controller
public class MainController {

	@Resource(name="bookService")
	BookService bookService;
	
	@Resource(name="userService")
	UserService userService;
	
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
	public String main(ModelMap mm) {
		Account account = SessionUtil.getSessionAccount();
		
		mm.addAttribute("bookList", bookService.getBookList(account.getIdx()));
		mm.addAttribute("memberName", account.getUserName());
		mm.addAttribute("footbar", "home");
		
		List<RentalHistory> myRentalList = userService.getMyNotReturedBooks(account.getIdx());
		List<RentalHistory> myRentalHistoryList = userService.getMyRentalHistories(account.getIdx());
		
		
		if(myRentalList != null && myRentalList.size() > 0) {
			mm.addAttribute("myRentalList", myRentalList);
		}
		
		if(myRentalHistoryList != null && myRentalHistoryList.size() > 0) {
			mm.addAttribute("myRentalHistoryList", myRentalHistoryList);
		}
		
		return "main";
	}
	
	@GetMapping("signin")
	public String getSigninPage() {
		return "signin";
	}
}
