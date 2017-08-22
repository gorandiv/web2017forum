package model;

public class Message {

	private String sender;
	private String receiver;
	private String content;
	private boolean seen;
	
	public Message() {
		// receiverDO Aureceiver-generated construcreceiverr stub
	}
	
	
	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getReceiver() {
		return receiver;
	}


	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


	public Message(String sender, String receiver, String content, boolean seen) {
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.seen = seen;
	}


	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	
	
}
