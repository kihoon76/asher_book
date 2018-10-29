package net.asher.book.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.asher.book.domain.Account;
import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.Book;
import net.asher.book.domain.RentalHistory;
import net.asher.book.service.BookService;
import net.asher.book.service.UserService;
import net.asher.book.util.SessionUtil;
import net.asher.book.websocket.AsherWebSocketHandler;

@Controller
@RequestMapping("/book")
public class BookController {
	
	@Resource(name="bookService")
	BookService bookService;
	
	@Resource(name="userService")
	UserService userService;
	
	@GetMapping("{bookNum}")
	public String getBookInfo(@PathVariable("bookNum") String bookNum, ModelMap mm) {
		String memberIdx = SessionUtil.getSessionUserIdx();
		mm.addAttribute("bookList", getBookList(memberIdx));
		mm.addAttribute("selectedBook", bookNum);
		mm.addAttribute("bookNum", bookNum);
		mm.addAttribute("bookInfo", bookService.getBookDetail(bookNum));
		mm.addAttribute("readMembers", bookService.getReadMemberList(bookNum));
		mm.addAttribute("kakao", String.valueOf(System.nanoTime()));
		return "book/bookTemplate";
		
	}
	
	@GetMapping("rental_history")
	public String getRentalHistory(ModelMap mm) throws IOException {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		List<Book> list = getBookList(memberIdx);
		List<Book> rentaledList = bookService.getRentaledBookList(memberIdx);
		
		String rentaledListStr = "";
		
		if(rentaledList != null || rentaledList.size() > 0) {
			rentaledListStr = new Gson().toJson(rentaledList);
		}
		
		mm.addAttribute("bookList", list);
		mm.addAttribute("memberIdx", memberIdx);
		mm.addAttribute("footbar", "reservation");
		mm.addAttribute("rentaledListStr", rentaledListStr);
		return "book/rentalHistory";
	}
	
	@GetMapping("reserve/booklist")
	@ResponseBody
	public AjaxVO<Book> getRentaledBookList() {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		AjaxVO<Book> vo = new AjaxVO<>();
		
		try {
			List<Book> rentaledList = bookService.getRentaledBookList(memberIdx);
			vo.setSuccess(true);
			vo.setDatas(rentaledList);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrMsg(e.getMessage());
			vo.setErrCode("etc");
		}
	
		return vo;
	}
	
	@GetMapping("rental_manage")
	public String getRentalManage(ModelMap mm) throws IOException {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		List<RentalHistory> list = userService.getRentalList("R");
		mm.addAttribute("bookList", getBookList(memberIdx));
		
		if(list != null && list.size() > 0) {
			Map<String, ArrayList<RentalHistory>> rm = new LinkedHashMap<>();
			
			int cnt = list.size();
			String currentMember = "";
			
			for(int i=0; i<cnt; i++) {
				if(currentMember.equals(list.get(i).getRentalManIdx())) {
					rm.get(currentMember).add(list.get(i));
				}
				else {
					currentMember = list.get(i).getRentalManIdx();
					rm.put(currentMember, new ArrayList<RentalHistory>());
					rm.get(currentMember).add(list.get(i));
				}
				
				
			}
			
			ObjectMapper om = new ObjectMapper();
			System.err.println(om.writeValueAsString(rm));
			
			mm.addAttribute("rentalMap", rm);
		}
		//mm.addAttribute("bookList", list);
		return "book/rentalManage";
	}
	
	@PostMapping("reserve/members")
	@ResponseBody
	public AjaxVO<Map<String, String>> getReserveMembers(@RequestParam("reserveBookNum") String reserveBookNum) {
		
		AjaxVO<Map<String, String>> vo = new AjaxVO<>();
		
		try {
			List<Map<String, String>> members = userService.getReserveMembers(reserveBookNum);
			
			vo.setSuccess(true);
			vo.setDatas(members);
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrCode("etc");
			vo.setErrMsg(e.getMessage());
		}
		
		return vo;
	}
	
	@PostMapping("reserve")
	@ResponseBody
	public AjaxVO<Map<String, String>>reserveBook(@RequestParam("reserveBookNum") String reserveBookNum) {
		
		
		AjaxVO<Map<String, String>> vo = new AjaxVO<>();
		
		try {
			boolean r = bookService.isRentaledBook(reserveBookNum);
			if(r) {
				Map<String, String> param = new HashMap<>();
				param.put("memberIdx", SessionUtil.getSessionUserIdx());
				param.put("bookNum", reserveBookNum);
				
				boolean isReg = userService.isAlreadyReservation(param);
				if(isReg) {
					vo.setSuccess(false);
					vo.setErrCode("606");
				}
				else {
					List<Map<String, String>> list = bookService.regReservation(param);
					vo.setSuccess(true);
					vo.setDatas(list);
				}
			}
			else {
				vo.setSuccess(false);
				vo.setErrCode("605");
			}
		}
		catch(Exception e) {
			vo.setSuccess(false);
			vo.setErrCode("etc");
			vo.setErrMsg(e.getMessage());
		}
		
		
		return vo;
	}
	
	private List<Book> getBookList(String memberIdx) {
		
		return bookService.getBookList(memberIdx);
	}
}
