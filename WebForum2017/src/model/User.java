package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Role role;
	private String contactPhone;
	private String email;
	private String dateOfRegistration;
	private ArrayList<Subforum> listOfSubscribedSubforums = new ArrayList<Subforum>();
	private ArrayList<Theme> listOfSavedThemes = new ArrayList<Theme>();
	private ArrayList<Comment> listOfSavedComments = new ArrayList<Comment>();

	public User() {
	}

	public User(String username, String password, String firstName, String lastName, String contactPhone, String email,
			String dateOfRegistration, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.contactPhone = contactPhone;
		this.email = email;
		this.dateOfRegistration = dateOfRegistration;
	}

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public enum Role {
		USER, MODERATOR, ADMINISTRATOR
	};

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDateOfRegistration() {
		return dateOfRegistration;
	}

	public void setDateOfRegistration(String dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}

	public ArrayList<Subforum> getListOfSubscribedSubforums() {
		return listOfSubscribedSubforums;
	}

	public void setListOfSubscribedSubforums(ArrayList<Subforum> listOfSubscribedSubforums) {
		this.listOfSubscribedSubforums = listOfSubscribedSubforums;
	}

	public ArrayList<Theme> getListOfSavedThemes() {
		return listOfSavedThemes;
	}

	public void setListOfSavedThemes(ArrayList<Theme> listOfSavedThemes) {
		this.listOfSavedThemes = listOfSavedThemes;
	}

	public ArrayList<Comment> getListOfSavedComments() {
		return listOfSavedComments;
	}

	public void setListOfSavedComments(ArrayList<Comment> listOfSavedComments) {
		this.listOfSavedComments = listOfSavedComments;
	}

	@Override
	public String toString() {
		return "firstName : " + this.firstName + ", lastName: " + this.lastName;
	}

	public void addSubforum(Subforum subforum) {
		if (listOfSubscribedSubforums == null) {
			this.listOfSubscribedSubforums = new ArrayList<Subforum>();
			listOfSubscribedSubforums.add(subforum);
		} else {
			this.listOfSubscribedSubforums.add(subforum);
		}
	}

	public void removeSubforum(Subforum subforum) {
		this.listOfSubscribedSubforums.remove(subforum);
	}
}
