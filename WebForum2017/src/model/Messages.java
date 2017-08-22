package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

public class Messages {

	private ArrayList<Message> messages;

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> Messages) {
		this.messages = Messages;
	}

	public Messages() {
		messages = new ArrayList<Message>();
	}

	public Messages(String realPath) {
		messages = new ArrayList<Message>();
		readMessages(realPath);
	}

	public void readMessages(String realPath) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Messages us = mapper.readValue(new File(realPath + "/data/messages.json"), Messages.class);
			this.messages = us.getMessages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fillInbox(User user) {
		for (Message message : messages) {
			if (message.getReceiver().equals(user.getUsername())) {
				// user.getInbox().add(message);
				// System.out.println(user.getUsername() + " inbox size : "
				// +user.getInbox().size());
			}
		}
	}

	public void newMessage(Message message, String realPath) {
		this.messages.add(message);
		writeMessages(realPath);
	}

	public synchronized void writeMessages(String realPath) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File(realPath + "/data/messages.json"), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
