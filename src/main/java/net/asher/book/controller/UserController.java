package net.asher.book.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.asher.book.domain.AjaxVO;
import net.asher.book.domain.RentalHistory;
import net.asher.book.service.UserService;
import net.asher.book.util.SessionUtil;

@RequestMapping("/user")
@Controller
public class UserController {

	@Resource(name="userService")
	UserService userService;
	
	@PostMapping("rental")
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
					vo.setSuccess(true);
				}
				else {
					vo.setSuccess(false);
					vo.setErrCode("601");
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
}
