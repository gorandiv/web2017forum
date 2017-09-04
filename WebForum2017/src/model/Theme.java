package model;

public class Theme {

	private Subforum themesSubforum;
	private String name;
	private Type type;
	private String author;
	private Comment comments;
	private String content;
	private String dateOfCreating;
	private int like;
	private int dislike;

	public Theme() {

	}

	public Theme(Subforum themesSubforum, String name, Type type, String author, Comment comments, String content,
			String dateOfCreating, int like, int dislike) {
		super();
		this.themesSubforum = themesSubforum;
		this.name = name;
		this.type = type;
		this.author = author;
		this.comments = comments;
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

}
