package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

public class Users {

	private ArrayList<User> users;
	
	public Users() {
		// TODO Auto-generated constructor stub
		users = new ArrayList<User>();
	}
	
	public Users(String realPath){
		users = new ArrayList<User>();
		readUsers(realPath);
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	public boolean addUser(User user){
		for(User u : users){
			if(u.getUsername().equals(user.getUsername()))
				return false;
		}
		users.add(user);
		return true;
	}
	
	public User getUser(String un, String pass){
		for(User u : users){
			if(u.getUsername().equals(un) && u.getPassword().equals(pass))
				return u;
		}
		
		return null;
	}
	
	public User getUserByUserName(String name){
		for(User u : this.users){
			if(u.getUsername().equals(name))
				return u;
		}
		
		return null;
	}
	
	private void readUsers(String realPath){
		try {
			ObjectMapper mapper = new ObjectMapper();
			Users us = mapper.readValue(new File(realPath + "/data/users.json"), Users.class);
			this.users = us.getUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void writeUsers(String realPath){
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File(realPath+"/data/users.json"), this);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
}
