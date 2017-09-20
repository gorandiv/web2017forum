package model;

import java.util.ArrayList;

public class Theme {

	private Subforum themesSubforum;
	private String name;
	private Type type;
	private String author;
	private String content;
	private String dateOfCreating;
	private int like;
	private int dislike;
	private ArrayList<String> usersLiked = new ArrayList<String>();
	private ArrayList<String> usersDisliked = new ArrayList<String>();
	private ArrayList<Comment> listOfComments = new ArrayList<Comment>();

	public Theme() {
		super();
	}

	public Theme(Subforum themesSubforum, String name, Type type, String author, String content,
			String dateOfCreating, int like, int dislike) {
		super();
		this.themesSubforum = themesSubforum;
		this.name = name;
		this.type = type;
		this.author = author;
		this.content = content;
		this.dateOfCreating = dateOfCreating;
		this.like = like;
		this.dislike = dislike;

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateOfCreating() {
		return dateOfCreating;
	}

	public void setDateOfCreating(String dateOfCreating) {
		this.dateOfCreating = dateOfCreating;
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

	public ArrayList<String> getUsersLiked() {
		return usersLiked;
	}

	public void setUsersLiked(ArrayList<String> usersLiked) {
		this.usersLiked = usersLiked;
	}

	public ArrayList<String> getUsersDisliked() {
		return usersDisliked;
	}

	public void setUsersDisliked(ArrayList<String> usersDisliked) {
		this.usersDisliked = usersDisliked;
	}

	public ArrayList<Comment> getListOfComments() {
		return listOfComments;
	}

	public void setListOfComments(ArrayList<Comment> listOfComments) {
		this.listOfComments = listOfComments;
	}
	
	public void addComment(Comment comment){
		if(listOfComments == null){
			this.listOfComments = new ArrayList<Comment>();
			this.listOfComments.add(comment);
		}else{
			this.listOfComments.add(comment);
		}
	}
	
	public void removeComment(Comment comment){
		listOfComments.remove(comment);
	}
	
	public void addUserLiked(String username) {
		if(usersLiked == null){
			usersLiked = new ArrayList<String>();
			usersLiked.add(username);
		}else{
			usersLiked.add(username);
		}
	}
	
	public void removeUserLiked(String username){
		usersLiked.remove(username);
	}
	
	public void addUserDisliked(String username) {
		if(usersDisliked == null){
			usersDisliked = new ArrayList<String>();
			usersDisliked.add(username);
		}else{
			usersDisliked.add(username);
		}
	}
	
	public void removeUserDisliked(String username){
		usersDisliked.remove(username);
	}
	

}
