package model;

import java.sql.Date;

public class Theme {

		private Subforum themesSubforum;
		private String name;
		private Type type;
		private User author;
		private Comment comments;
		private String content;
		private Date DateOfCreating;
		private int like;
		private int dislike;
		
		public Theme(){
			
		}
		
		public Subforum getThemesSubforum() {
			return themesSubforum;
		}
		public void setThemesSubforum(Subforum themesSubforum) {
			this.themesSubforum = themesSubforum;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}
		public User getAuthor() {
			return author;
		}
		public void setAuthor(User author) {
			this.author = author;
		}
		public Comment getComments() {
			return comments;
		}
		public void setComments(Comment comments) {
			this.comments = comments;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Date getDateOfCreating() {
			return DateOfCreating;
		}
		public void setDateOfCreating(Date dateOfCreating) {
			DateOfCreating = dateOfCreating;
		}
		public int getLike() {
			return like;
		}
		public void setLike(int like) {
			this.like = like;
		}
		public int getDislike() {
			return dislike;
		}
		public void setDislike(int dislike) {
			this.dislike = dislike;
		}
		
		
		
		
}
