package net.asher.book.util;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

public class RestClient {

	private String server = "https://apis.aligo.in:443";
	private RestTemplate rest;
	private HttpHeaders headers;
	private HttpStatus status;
	private String key;
	private String userId;
	private String sender;
	
	public RestClient(String key, String userId, String sender) {
		this.rest = new RestTemplate();
		this.rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	    this.headers = new HttpHeaders();
	    this.key = key;
	    this.userId = userId;
	    this.sender = sender;
	    
	    headers.add("Content-Type", "application/x-www-form-urlencoded");
	    headers.add("Accept", "*/*");
	}
	
	public String get(String uri) {
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
		this.setStatus(responseEntity.getStatusCode());
		return responseEntity.getBody();
	}

	public String post(String uri, Map<String, String> map) { 
		map.put("key", key);
		map.put("user_id", userId);
		map.put("sender", sender);
		
		String json = new Gson().toJson(map);
	    HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
	    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
	    this.setStatus(responseEntity.getStatusCode());
	    return responseEntity.getBody();
	}


	public HttpStatus getStatus() {
	    return status;
	}

	public void setStatus(HttpStatus status) {
	    this.status = status;
	} 
}
