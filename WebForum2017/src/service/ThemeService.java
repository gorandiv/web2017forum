package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
		System.out.println("usao sam u createTheme preko RESTa");
		ArrayList<Theme> listOfTheme = ThemeFileController.readTheme(config);

		for (Theme tt : listOfTheme) {
			if (tt.getName().equals(t.getName())) {
				return "Theme already exist";
			}
		}
		Theme theme = new Theme(t.getThemesSubforum(), t.getName(), null, t.getAuthor(), null, t.getContent(),
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

		/*
		 * for (int i = 0; i < listOfUsers.size(); i++) { ArrayList<Subforum>
		 * listOfFollowedSubforum =
		 * listOfUsers.get(i).getListOfSubscribedSubforums(); for (int k = 0; k
		 * < listOfFollowedSubforum.size(); k++) { if
		 * (listOfFollowedSubforum.get(k).getName().equals(theme.getName())) {
		 * listOfUsers.get(i).removeSubforum(listOfFollowedSubforum.get(k)); } }
		 * }
		 * 
		 * UserFileController.writeUser(config, listOfUsers);
		 */

		for (int i = 0; i < listOfThemes.size(); i++) {
			if (listOfThemes.get(i).getName().equals(theme.getName())) {
				listOfThemes.remove(i);
			}
		}
		ThemeFileController.writeTheme(config, listOfThemes);
		return "theme deleted!";
	}

}
