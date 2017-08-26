package model;

import java.util.ArrayList;

public class Subforum {

	public Subforum(String name, String description, String icon, String listOfRules, String responsibleModerator,
			ArrayList<User> moderators) {
		super();
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.listOfRules = listOfRules;
		this.responsibleModerator = responsibleModerator;
		this.moderators = moderators;
	}

	private String name;
	private String description;
	private String icon;
	private String listOfRules;
	private String responsibleModerator;
	private ArrayList<User> moderators;

	public Subforum() {
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

	public String getListOfRules() {
		return listOfRules;
	}

	public void setListOfRules(String listOfRules) {
		listOfRules = listOfRules;
	}

	public String getResponsibleModerator() {
		return responsibleModerator;
	}

	public void setResponsibleModerator(String responsibleModerator) {
		responsibleModerator = responsibleModerator;
	}

	public ArrayList<User> getModerators() {
		return moderators;
	}

	public void setModerators(ArrayList<User> moderators) {
		moderators = moderators;
	}

}
