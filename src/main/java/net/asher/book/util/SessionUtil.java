package net.asher.book.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.asher.book.domain.Account;


public class SessionUtil {

	public static String getSessionUserIdx() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account user = (Account)auth.getPrincipal();
		return user.getIdx();
	}
	
	public static Account getSessionAccount() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account user = (Account)auth.getPrincipal();
		return user;
	}
}
