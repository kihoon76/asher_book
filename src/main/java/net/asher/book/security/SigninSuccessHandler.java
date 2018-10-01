package net.asher.book.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import net.asher.book.domain.Account;
import net.asher.book.domain.AjaxVO;

@Component
public class SigninSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		String accept = request.getHeader("accept");
		
		if(true/*StringUtils.indexOf(accept, "json") > -1*/) {
			//response.setContentType("application/json");
			//response.setContentType("utf-8");
		
			response.setContentType("text/plain;charset=utf-8");
			//Cookie 처리
			//String idSave = request.getParameter("idsave");
			//String id = request.getParameter("id");
			
			/*Cookie cookie = new Cookie("id", id);
			cookie.setMaxAge((idSave != null) ? 1000 : 0);
			response.addCookie(cookie);*/
			
			Account user = (Account)authentication.getPrincipal();
//			
//			ObjectMapper om = new ObjectMapper();
//			System.err.println(om.writeValueAsString(user.getAuthorities()));
			
			//만기일 1주일전 체크
//			List<Map<String, String>> list = new ArrayList<>();
//			
//			for(Authority auth : user.getAuthorities()) {
//				if(!"ROLE_MEMBER".equals(auth.getAuthName()) && 
//				   !"ROLE_ADMIN".equals(auth.getAuthName()) &&
//				   !"ROLE_QA_ADMIN".equals(auth.getAuthName())) {
//					
//					long remainDate = 0;
//					
//					try {
//						if((remainDate = DateUtil.diffOfDate(auth.getExpire())) <= 7) {
//							Map<String, String> m = new HashMap<>();
//							m.put("authName", StringUtil.getAuthNameKor(auth.getAuthName()));
//							m.put("expire", auth.getExpire());
//							m.put("remain", String.valueOf(remainDate));
//							
//							list.add(m);
//						}
//					} 
//					catch (ParseException e) {
//						
//					}
//				}
//			}
			
			AjaxVO data = new AjaxVO();
			data.setSuccess(true);
			//data.setDatas(user.getAuthorities());
			PrintWriter out = response.getWriter();
			//PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8), true);
			out.print(new Gson().toJson(data));
			out.flush();
			out.close();
		}

	}
}
