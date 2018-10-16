package net.asher.book.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeaderUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpHeaderUtil.class);
	private static String[] mobileOs = {"iPhone", "iPad", "Android", "BlackBerry", "Windows CE", "Nokia", "Webos", "Opera Mini", "SonyEricsson", "Opera Mobi", "IEMobile"};
	
	public static String getClientIP(HttpServletRequest request) {
		
		/*
		 * https://www.lesstif.com/pages/viewpage.action?pageId=20775886
		 * 
		 * */
		 String ip = request.getHeader("X-Forwarded-For");
		 String header = "X-Forwarded-For";
		 
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		     ip = request.getHeader("Proxy-Client-IP");
		     header = "Proxy-Client-IP";
		 } 
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		     ip = request.getHeader("WL-Proxy-Client-IP");
		     header = "WL-Proxy-Client-IP";
		 } 
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		     ip = request.getHeader("HTTP_CLIENT_IP"); 
		     header = "HTTP_CLIENT_IP";
		 } 
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		     ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		     header = "HTTP_X_FORWARDED_FOR";
		 } 
	
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		     ip = request.getRemoteAddr(); 
		     header = "RemoteAddr";
		 }
		 
		logger.info("####################################################");
		logger.info("# " + header + " : " + ip);
		logger.info("####################################################");
		
		return ip;
	}
	
	public static boolean isByPassIP(String[] ips, HttpServletRequest request) {
		if(ips == null) return true;
		
		String myIp = getClientIP(request);
		int len = ips.length;
		
		for(int i=0; i<len; i++) {
			if(myIp.equals(ips[i].trim())) return true;
		}
		
		return false;
	}
	
	public static String getUrlRoot(HttpServletRequest request) {
		return request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()) + "/";
	}
	
	public static String isMobile(String ua) {
		
		String isMobile = "N";
		
		if(ua != null && !"".equals(ua)) {
			
			int len = mobileOs.length;
			int j = -1;
			
			for(int i=0; i<len; i++) {
				j = ua.indexOf(mobileOs[i]);
				
				if(j > -1) {
					isMobile = "Y";
					break;
				}
			}
		}
		
		return isMobile;
	}
}
