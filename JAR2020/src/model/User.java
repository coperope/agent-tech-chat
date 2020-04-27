package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private Host host;
	private HashMap<String, List<Message>> inbox =  new HashMap<>();;
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}
	
	public HashMap<String, List<Message>> getInbox() {
		return inbox;
	}

	public void setInbox(HashMap<String, List<Message>> inbox) {
		this.inbox = inbox;
	}
	
	public boolean receiveMessage(Message msg) {
		if(!inbox.containsKey(msg.getSender())) {
			List<Message> msgs = new ArrayList<Message>();
			msgs.add(msg);
			inbox.put(msg.getSender(), msgs);
		}else {
			if (inbox.containsKey(msg.getSender())) {
				List<Message> msgs = inbox.get(msg.getSender());
				msgs.add(msg);
			}else {
				return false;
			}
		}
		return true;
	}
	public boolean sendedMessage(Message msg) {
		if(!inbox.containsKey(msg.getReceiver())) {
			List<Message> msgs = new ArrayList<Message>();
			msgs.add(msg);
			inbox.put(msg.getReceiver(), msgs);
		}else {
			if (inbox.containsKey(msg.getReceiver())) {
				List<Message> msgs = inbox.get(msg.getReceiver());
				msgs.add(msg);
			}else {
				return false;
			}
		}
		return true;
	}
	
	public User() {
	}

	public User(String username, String password, Host host) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.inbox = new HashMap<>();
	}

	@Override
	public String toString() {
		return "User:" + username;
	}

}