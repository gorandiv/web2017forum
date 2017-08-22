package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import file.UserFileController;
import model.User;
import model.User.Role;

@Path("/users")
public class UserService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	@Context
	ServletConfig config;

	@POST
	@Path("/getUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(User user) throws FileNotFoundException, IOException {
		ArrayList<User> listOfUser = UserFileController.readUser(config);

		for (User u : listOfUser) {
			if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				return u;
			}
		}

		return null;
	}

	@POST
	@Path("/addUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addUser(User u) throws FileNotFoundException, IOException {
		ArrayList<User> listOfUser = UserFileController.readUser(config);

		for (User user : listOfUser) {
			if (user.getUsername().equals(u.getUsername())) {
				return "User already exist";
			}
		}

		listOfUser.add(u);
		UserFileController.writeUser(config, listOfUser);
		return " Registered monkey";
	}

	@GET
	@Path("/getUsers/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getUsers(@PathParam(value = "username") String username)
			throws FileNotFoundException, IOException {
		ArrayList<User> listOfUser = UserFileController.readUser(config);
		ArrayList<User> newListOfUser = new ArrayList<User>();

		for (User u : listOfUser) {
			if (u.getUsername().equals(username)) {
				System.out.println("nasao je mene");
			} else {
				newListOfUser.add(u);
			}
		}

		return newListOfUser;
	}

	@POST
	@Path("/editUserRole/{username}/{role}")
	@Produces(MediaType.TEXT_PLAIN)
	public String editUserRole(@PathParam(value = "username") String username, @PathParam(value = "role") String role)
			throws FileNotFoundException, IOException {
		ArrayList<User> listOfUser = UserFileController.readUser(config);
		for (User u : listOfUser) {
			if (u.getUsername().equals(username)) {
				u.setRole(Role.valueOf(role));
			}
		}

		UserFileController.writeUser(config, listOfUser);
		return "User role changed";
	}

}