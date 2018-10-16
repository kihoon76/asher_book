package net.asher.book.domain;

public class Email {

	private String subject = "아셀교회에서 알려드립니다.";
	private Account account;
	private String accountId;
	private String content = "";
	private String url;
	
	public Email(String url) {
		this.url = url;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmailBody() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<table style=\"width:500px;\">");
		sb.append("<tbody>");
		sb.append("<tr style=\"background-color:#333336\">");
		sb.append("<td><img src=\"" + url + "resources/img/logo.jpg\" /></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>" + content + "</td>");
		sb.append("</tr>");
		
		sb.append("</tbody>");
		sb.append("</table>");
		
		
		return sb.toString();
	}
	
	
}
