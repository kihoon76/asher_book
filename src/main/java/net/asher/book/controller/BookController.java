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
import net.asher.book.websocket.AsherWebSocketHandler;

@Controller
@RequestMapping("/book")
public class BookController {
	
	@Resource(name="bookService")
	BookService bookService;
	
	@Resource(name="asherWebSocketHandler")
	AsherWebSocketHandler asherWebSocketHandler;
	
	@GetMapping("{bookNum}")
	public String getBookInfo(@PathVariable("bookNum") String bookNum, ModelMap mm) {
		mm.addAttribute("bookList", getBookList());
		mm.addAttribute("selectedBook", bookNum);
		mm.addAttribute("bookNum", bookNum);
		return "book/bookTemplate";
		
	}
	
	@GetMapping("rental_history")
	public String getRentalHistory(ModelMap mm) throws IOException {
		asherWebSocketHandler.sendDatabaseMsg("test");
		List<Book> list = getBookList();
		mm.addAttribute("bookList", list);
		return "book/rentalHistory";
	}
	
	private List<Book> getBookList() {
		
		return bookService.getBookList();
	}
}
