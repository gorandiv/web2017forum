package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import file.ThemeFileController;
import file.UserFileController;
import model.Theme;
import model.User;

@Path("/themes")
public class ThemeService {

	@Context
	ServletConfig config;

	@POST
	@Path("/createTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String createTheme(Theme t) throws FileNotFoundException, IOException {
		ArrayList<Theme> listOfTheme = ThemeFileController.readTheme(config);

		for (Theme tt : listOfTheme) {
			if (tt.getName().equals(t.getName())) {
				return "Theme already exist";
			}
		}
		Theme theme = new Theme(t.getThemesSubforum(), t.getName(), t.getType(), t.getAuthor(), null, t.getContent(),
				t.getDateOfCreating(), 0, 0);
		listOfTheme.add(theme);
		ThemeFileController.writeTheme(config, listOfTheme);

		return "Registered theme";
	}

	@GET
	@Path("/getTheme")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> getTheme() throws FileNotFoundException, IOException {
		ArrayList<Theme> listOfThemes = ThemeFileController.readTheme(config);

		return listOfThemes;
	}

	@POST
	@Path("/deleteTheme")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteTheme(Theme theme) throws FileNotFoundException, IOException {
		ArrayList<Theme> listOfThemes = ThemeFileController.readTheme(config);
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		System.out.println("usao sam u deleteTheme java metoda");

		for (int i = 0; i < listOfUsers.size(); i++) {
			ArrayList<Theme> listOfSavedTheme = listOfUsers.get(i).getListOfSavedThemes();
			for (int k = 0; k < listOfSavedTheme.size(); k++) {
				if (listOfSavedTheme.get(k).getName().equals(theme.getName())) {
					listOfUsers.get(i).removeTheme(listOfSavedTheme.get(k));
				}
			}
		}

		UserFileController.writeUser(config, listOfUsers);

		for (int i = 0; i < listOfThemes.size(); i++) {
			if (listOfThemes.get(i).getName().equals(theme.getName())) {
				listOfThemes.remove(i);
			}
		}
		ThemeFileController.writeTheme(config, listOfThemes);
		return "theme deleted!";
	}

	@POST
	@Path("/saveTheme/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveTheme(@PathParam(value = "username") String username, Theme theme)
			throws FileNotFoundException, IOException {
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		for (User u : listOfUsers) {
			if (u.getUsername().equals(username)) {
				u.addTheme(theme);
			}
		}
		UserFileController.writeUser(config, listOfUsers);
		return "Theme saved";
	}

	@GET
	@Path("/getSavedTheme/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> getSavedTheme(@PathParam(value = "username") String username)
			throws FileNotFoundException, IOException {
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		for (User u : listOfUsers) {
			if (u.getUsername().equals(username)) {
				return u.getListOfSavedThemes();
			}
		}
		return null;
	}

	@POST
	@Path("/editTheme/{name}/{content}")
	@Produces(MediaType.TEXT_PLAIN)
	public String editTheme(@PathParam(value = "name") String name, @PathParam(value = "content") String content)
			throws FileNotFoundException, IOException {
		ArrayList<Theme> listOfThemes = ThemeFileController.readTheme(config);
		for (Theme t : listOfThemes) {
			if (t.getName().equals(name)) {
				t.setContent(content);
			}
		}

		ThemeFileController.writeTheme(config, listOfThemes);
		return "Theme edited!";
	}

}
