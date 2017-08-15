package model;

import java.util.ArrayList;
import java.util.Date;

public class User {

		private String username;
		private String password;
		private String firstName;
		private String lastName;
		private String role;
		private String contactPhone;
		private String email; 
		private Date dateOfRegistration;
		private ArrayList<Subforum> listOfSubscirebedSubforums;
		private ArrayList<Theme> listOfSavedThemes;
		private ArrayList<Comment> listOfSavedComments;
		
		
		public User() {
			super();
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
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String getContactTelephone() {
			return contactPhone;
		}
		public void setContactTelephone(String contactTelephone) {
			this.contactPhone = contactTelephone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Date getDateOfRegistration() {
			return dateOfRegistration;
		}
		public void setDateOfRegistration(Date dateOfRegistration) {
			this.dateOfRegistration = dateOfRegistration;
		}
		public ArrayList<Subforum> getListOfSubscirebedSubforums() {
			return listOfSubscirebedSubforums;
		}
		public void setListOfSubscirebedSubforums(ArrayList<Subforum> listOfSubscirebedSubforums) {
			this.listOfSubscirebedSubforums = listOfSubscirebedSubforums;
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
		
		
		
		
}
