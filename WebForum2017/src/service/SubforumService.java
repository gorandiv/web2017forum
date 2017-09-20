package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import file.SubforumFileController;
import file.UserFileController;
import model.Subforum;
import model.User;

@Path("/subforums")
public class SubforumService {

	@Context
	ServletConfig config;

	@POST
	@Path("/addSubforum")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String addSubforum(@FormDataParam("name") String name, @FormDataParam("description") String description,
			@FormDataParam("responsibleModerator") String responsibleModerator, @FormDataParam("file") InputStream in,
			@FormDataParam("file") FormDataContentDisposition info) throws FileNotFoundException, IOException {

		System.out.println("evo me u addSubforum java service");

		String s = config.getServletContext().getRealPath("");
		String path = getMediaPath(s).getPath();

		ArrayList<Subforum> listOfSubforum = SubforumFileController.readSubforum(config);

		for (Subforum sf : listOfSubforum) {
			if (sf.getName().equals(name)) {
				return "Subforum already exist";
			}
		}

		Subforum subforum = new Subforum(name, description, null, null, responsibleModerator, null);
		listOfSubforum.add(subforum);
		SubforumFileController.writeSubforum(config, listOfSubforum);

		try {
			int read = 0;
			File file = new File(path + "//" + "icons" + "//" + info.getFileName());
			for (Subforum sub : listOfSubforum) {
				if (sub.getName().equals(subforum.getName())) {
					sub.setIcon(file.getName());
					SubforumFileController.writeSubforum(config, listOfSubforum);
					break;
				}
			}
			byte[] bytes = new byte[1024];
			OutputStream out = new FileOutputStream(file);
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			return "Internal server error";
		}

		return "Registered subforum";

	}

	public File getMediaPath(String s) {
		File pathD = new File(s);
		if (!pathD.exists()) {
			pathD.mkdir();
		}
		return pathD;
	}

	@GET
	@Path("/getSubforum")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Subforum> getSubforum() throws FileNotFoundException, IOException {
		ArrayList<Subforum> listOfSubforum = SubforumFileController.readSubforum(config);

		return listOfSubforum;
	}

	@POST
	@Path("/followSubforum/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String followSubforum(@PathParam(value = "username") String username, Subforum subforum)
			throws FileNotFoundException, IOException {
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		for (User u : listOfUsers) {
			if (u.getUsername().equals(username)) {
				u.addSubforum(subforum);
				break;
			}
		}
		UserFileController.writeUser(config, listOfUsers);
		return "Subforum are followed";
	}

	@GET
	@Path("/getFollowedSubforum/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Subforum> getFollowedSubforum(@PathParam(value = "username") String username)
			throws FileNotFoundException, IOException {
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		for (User u : listOfUsers) {
			if (u.getUsername().equals(username)) {
				return u.getListOfSubscribedSubforums();
			}
		}
		return null;
	}

	@POST
	@Path("/deleteSubforum")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteSubforum(Subforum subforum) throws FileNotFoundException, IOException {
		ArrayList<Subforum> listOfSubforums = SubforumFileController.readSubforum(config);
		ArrayList<User> listOfUsers = UserFileController.readUser(config);
		System.out.println("usao sam u deleteSubforum java metoda");

		for (int i = 0; i < listOfUsers.size(); i++) {
			ArrayList<Subforum> listOfFollowedSubforum = listOfUsers.get(i).getListOfSubscribedSubforums();
			for (int k = 0; k < listOfFollowedSubforum.size(); k++) {
				if (listOfFollowedSubforum.get(k).getName().equals(subforum.getName())) {
					listOfUsers.get(i).removeSubforum(listOfFollowedSubforum.get(k));
				}
			}
		}

		UserFileController.writeUser(config, listOfUsers);

		for (int i = 0; i < listOfSubforums.size(); i++) {
			if (listOfSubforums.get(i).getName().equals(subforum.getName())) {
				listOfSubforums.remove(i);
				break;
			}
		}
		SubforumFileController.writeSubforum(config, listOfSubforums);
		return "subforum deleted!";
	}
	
	@GET
	@Path("/searchSubforums/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Subforum> searchSubforums(@PathParam(value = "name") String name) throws FileNotFoundException, IOException{
		ArrayList<Subforum> listOfSubforum =  SubforumFileController.readSubforum(config);
		ArrayList<Subforum> searchedSubforums = new ArrayList<Subforum>();
		
		
		for(Subforum s : listOfSubforum){
			if(s.getName().startsWith(name)){
				searchedSubforums.add(s);
			}
		}
		
		return searchedSubforums;
	}

}
