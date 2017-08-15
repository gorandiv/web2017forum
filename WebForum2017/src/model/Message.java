package model;

public class Message {

		private User sender;
		private User receiver;
		private String content;
		private boolean tag;
		
		public Message(){
			
		}
		
		public User getSender() {
			return sender;
		}
		public void setSender(User sender) {
			this.sender = sender;
		}
		public User getReceiver() {
			return receiver;
		}
		public void setReceiver(User receiver) {
			this.receiver = receiver;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public boolean isTag() {
			return tag;
		}
		public void setTag(boolean tag) {
			this.tag = tag;
		}
}
