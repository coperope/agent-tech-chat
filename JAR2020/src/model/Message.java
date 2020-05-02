package model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String sender;
	private String receiver;
	private Date date;
	private String subject;
	private String content;
	
	public Message() {}
	
	public Message(String from, String to, String subject, String content) {
		super();
		this.sender = from;
		this.receiver = to;
		this.date = new Date(System.currentTimeMillis());
		this.subject = subject;
		this.content = content;
	}


	public Message(String from, String to, Date date, String subject, String content) {
		super();
		this.sender = from;
		this.receiver = to;
		this.date = date;
		this.subject = subject;
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String from) {
		this.sender = from;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String to) {
		this.receiver = to;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}