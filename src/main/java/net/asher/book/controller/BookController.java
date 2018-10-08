package net.asher.book.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.Book;
import net.asher.book.service.BookService;
import net.asher.book.util.SessionUtil;
import net.asher.book.websocket.AsherWebSocketHandler;

@Controller
@RequestMapping("/book")
public class BookController {
	
	@Resource(name="bookService")
	BookService bookService;
	
	@GetMapping("{bookNum}")
	public String getBookInfo(@PathVariable("bookNum") String bookNum, ModelMap mm) {
		String memberIdx = SessionUtil.getSessionUserIdx();
		mm.addAttribute("bookList", getBookList(memberIdx));
		mm.addAttribute("selectedBook", bookNum);
		mm.addAttribute("bookNum", bookNum);
		return "book/bookTemplate";
		
	}
	
	@GetMapping("rental_history")
	public String getRentalHistory(ModelMap mm) throws IOException {
		String memberIdx = SessionUtil.getSessionUserIdx();
		
		List<Book> list = getBookList(memberIdx);
		mm.addAttribute("bookList", list);
		return "book/rentalHistory";
	}
	
	private List<Book> getBookList(String memberIdx) {
		
		return bookService.getBookList(memberIdx);
	}
}
