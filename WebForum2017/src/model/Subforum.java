package model;

import java.util.ArrayList;

public class Subforum {
	
		private String name;
		private String description;
		private String icon;
		private ArrayList<String> ListOfRules;
		private User ResponsibleModerator;
		private ArrayList<User> Moderators;
		
		public Subforum(){
			super();
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public ArrayList<String> getListOfRules() {
			return ListOfRules;
		}
		public void setListOfRules(ArrayList<String> listOfRules) {
			ListOfRules = listOfRules;
		}
		public User getResponsibleModerator() {
			return ResponsibleModerator;
		}
		public void setResponsibleModerator(User responsibleModerator) {
			ResponsibleModerator = responsibleModerator;
		}
		public ArrayList<User> getModerators() {
			return Moderators;
		}
		public void setModerators(ArrayList<User> moderators) {
			Moderators = moderators;
		}
		
		
}
