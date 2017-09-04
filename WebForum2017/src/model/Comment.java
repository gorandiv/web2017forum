package model;

import java.util.Date;

public class Comment {

	private Theme theme;
	private User Author;
	private Date dateOfComment;
	private Comment parentsComment;
	private Comment subComment;
	private String textOfComment;
	private int like;
	private int dislike;
	private boolean tagOfComment;

	public Comment() {

	}

	public Comment(Theme theme, User author, Date dateOfComment, Comment parentsComment, Comment subComment,
			String textOfComment, int like, int dislike, boolean tagOfComment) {
		super();
		this.theme = theme;
		Author = author;
		this.dateOfComment = dateOfComment;
		this.parentsComment = parentsComment;
		this.subComment = subComment;
		this.textOfComment = textOfComment;
		this.like = like;
		this.dislike = dislike;
		this.tagOfComment = tagOfComment;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public User getAuthor() {
		return Author;
	}

	public void setAuthor(User author) {
		Author = author;
	}

	public Date getDateOfComment() {
		return dateOfComment;
	}

	public void setDateOfComment(Date dateOfComment) {
		this.dateOfComment = dateOfComment;
	}

	public Comment getParentsComment() {
		return parentsComment;
	}

	public void setParentsComment(Comment parentsComment) {
		this.parentsComment = parentsComment;
	}

	public Comment getSubComment() {
		return subComment;
	}

	public void setSubComment(Comment subComment) {
		this.subComment = subComment;
	}

	public String getTextOfComment() {
		return textOfComment;
	}

	public void setTextOfComment(String textOfComment) {
		this.textOfComment = textOfComment;
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

	public boolean isTagOfComment() {
		return tagOfComment;
	}

	public void setTagOfComment(boolean tagOfComment) {
		this.tagOfComment = tagOfComment;
	}
}
