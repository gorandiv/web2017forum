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

import org.w3c.dom.ls.LSInput;

import file.CommentFileController;
import file.SubforumFileController;
import file.ThemeFileController;
import file.UserFileController;
import model.Comment;
import model.Subforum;
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
		ArrayList<Theme> listThemesOfSubforum = new ArrayList<Theme>();

		for(Theme tt : listOfTheme){
			if(t.getThemesSubforum().getName().equals(t.getThemesSubforum().getName()))
				listThemesOfSubforum.add(tt);
		}
		
		for (Theme tt : listThemesOfSubforum) {
			if(tt.getName().equals(t.getName()))
				return "Theme already exist";
		}
		Theme theme = new Theme(t.getThemesSubforum(), t.getName(), t.getType(), t.getAuthor(), t.getContent(),
				t.getDateOfCreating(), 0, 0);
		listOfTheme.add(theme);
		ThemeFileController.writeTheme(config, listOfTheme);

		return "Registered theme";
	}

	@GET
	@Path("/getTheme/{subforumName}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> getTheme(@PathParam(value = "subforumName") String subforumName) throws FileNotFoundException, IOException {
		ArrayList<Theme> listOfThemes = ThemeFileController.readTheme(config);
		ArrayList<Theme> listThemesOfSubforum = new ArrayList<Theme>();
		
		for(Theme t : listOfThemes){
			if(t.getThemesSubforum().getName().equals(subforumName)){
				listThemesOfSubforum.add(t);
			}
		}

		return listThemesOfSubforum;
	}

	@POST
	@Path("/deleteTheme")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteTheme(Theme theme) throws FileNotFoundException, IOException {
		ArrayList<Theme> listOfThemes = ThemeFileController.readTheme(config);
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		ArrayList<Comment> listoOfComments = CommentFileController.readComment(config);
		System.out.println("usao sam u deleteTheme java metoda");

		for (int i = 0; i < listOfUsers.size(); i++) {
			ArrayList<Theme> listOfSavedTheme = listOfUsers.get(i).getListOfSavedThemes();
			for (int k = 0; k < listOfSavedTheme.size(); k++) {
				if (listOfSavedTheme.get(k).getName().equals(theme.getName())) {
					listOfUsers.get(i).removeSavedTheme(listOfSavedTheme.get(k));
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
		
		for(int i = 0; i < listoOfComments.size(); i++){
			if(listoOfComments.get(i).getTheme().getName().equals(theme.getName())){
				listoOfComments.remove(i);
			}
		}
		CommentFileController.writeComment(config, listoOfComments);
		return "Theme deleted";
	}

	@POST
	@Path("/saveTheme/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveTheme(@PathParam(value = "username") String username, Theme theme)
			throws FileNotFoundException, IOException {
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		for (User u : listOfUsers) {
			if (u.getUsername().equals(username)) {
				u.saveTheme(theme);
				break;
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
	
	@POST
	@Path("/likeTheme/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	public String likeTheme(@PathParam(value = "username") String username, Theme theme) throws FileNotFoundException, IOException{
		ArrayList<Theme> themes = ThemeFileController.readTheme(config);
		ArrayList<User> users = UserFileController.readUser(config);
		
		for(int i = 0; i < themes.size(); i++){
			if(themes.get(i).getName().equals(theme.getName())){
				for(String user : themes.get(i).getUsersDisliked()){
					if(user.equals(username))
						return "You already disliked theme";
				}
				for(String user : themes.get(i).getUsersLiked()){
					if(user.equals(username))
						return "You already liked theme";
				}
				themes.get(i).setLike(themes.get(i).getLike() + 1);
				themes.get(i).addUserLiked(username);
				break;
			}
		}
		ThemeFileController.writeTheme(config, themes);
		for(User user : users){
			ArrayList<Theme> userThemes = user.getListOfSavedThemes();
			for(int j = 0; j < userThemes.size(); j++){
				if(userThemes.get(j).getName().equals(theme.getName())){
					userThemes.get(j).setLike(userThemes.get(j).getLike() + 1);
					userThemes.get(j).addUserLiked(username);
					break;
				}
			}
		}
		UserFileController.writeUser(config, users);
		return "Theme liked";
	}
	
	@POST
	@Path("/dislikeTheme/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	public String dislikeTheme(@PathParam(value = "username") String username, Theme theme) throws FileNotFoundException, IOException{
		ArrayList<Theme> themes = ThemeFileController.readTheme(config);
		ArrayList<User> users = UserFileController.readUser(config);
		
		for(int i = 0; i < themes.size(); i++){
			if(themes.get(i).getName().equals(theme.getName())){
				for(String user : themes.get(i).getUsersDisliked()){
					if(user.equals(username))
						return "You already disliked theme";
				}
				for(String user : themes.get(i).getUsersLiked()){
					if(user.equals(username))
						return "You already liked theme";
				}
				themes.get(i).setDislike(themes.get(i).getDislike() + 1);
				themes.get(i).addUserDisliked(username);
				break;
			}
		}
		ThemeFileController.writeTheme(config, themes);
		for(User user : users){
			ArrayList<Theme> userThemes = user.getListOfSavedThemes();
			for(int j = 0; j < userThemes.size(); j++){
				if(userThemes.get(j).getName().equals(theme.getName())){
					userThemes.get(j).setDislike(userThemes.get(j).getDislike() + 1);
					userThemes.get(j).addUserDisliked(username);
					break;
				}
			}
		}
		UserFileController.writeUser(config, users);
		return "Theme disliked";
	}
	
	@GET
	@Path("/searchThemes/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> searchThemes(@PathParam(value = "name") String name) throws FileNotFoundException, IOException{
		ArrayList<Theme> listOfThemes =  ThemeFileController.readTheme(config);
		ArrayList<Theme> searchedThemes = new ArrayList<Theme>();
		
		
		for(Theme t : listOfThemes){
			if(t.getName().startsWith(name)){
				searchedThemes.add(t);
			}
		}
		
		return searchedThemes;
	}
	
	

}
