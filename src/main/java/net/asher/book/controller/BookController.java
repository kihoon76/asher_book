package net.asher.book.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.Book;
import net.asher.book.service.BookService;

@Controller
@RequestMapping("/book")
public class BookController {
	
	@Resource(name="bookService")
	BookService bookService;

	@GetMapping("{bookNum}")
	public String getBookInfo(@PathVariable("bookNum") String bookNum, ModelMap mm) {
		mm.addAttribute("bookList", getBookList());
		mm.addAttribute("selectedBook", bookNum);
		mm.addAttribute("bookNum", bookNum);
		return "book/bookTemplate";
		
	}
	
	private List<Book> getBookList() {
		
		return bookService.getBookList();
	}
}
