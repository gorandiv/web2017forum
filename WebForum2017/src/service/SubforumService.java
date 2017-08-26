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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import file.SubforumFileController;
import model.Subforum;

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
	@Path("/deleteSubforum")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteSubforum(Subforum subforum) throws FileNotFoundException, IOException {
		ArrayList<Subforum> listOfSubforum = SubforumFileController.readSubforum(config);
		System.out.println("usao sam u deleteSubforum java metoda");
		for (int i = 0; i < listOfSubforum.size(); i++) {
			if (listOfSubforum.get(i).getName().equals(subforum.getName())) {
				listOfSubforum.remove(i);
			}
		}
		SubforumFileController.writeSubforum(config, listOfSubforum);
		return "subforum deleted!";
	}

}
