package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import model.User;

public class UserFileController {

	public UserFileController() {

	}

	public static ArrayList<User> readUser(ServletConfig config) throws FileNotFoundException, IOException {
		ArrayList<User> users = new ArrayList<User>();
		String path = FilePaths.getPath(config).getPath();
		ObjectMapper mapper = new ObjectMapper();

		File varTmpDir = new File(path + "//userData.txt");
		boolean exists = varTmpDir.exists();
		if (!exists) {
			try {
				PrintWriter wr = new PrintWriter(path + "//userData.txt");
				wr.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}

		FileInputStream fis = new FileInputStream(new File(path + "//userData.txt"));

		JsonFactory jf = new JsonFactory();
		JsonParser jp = jf.createParser(fis);
		jp.setCodec(mapper);
		jp.nextToken();

		while (jp.hasCurrentToken()) {
			User user = jp.readValueAs(User.class);
			users.add(user);
			jp.nextToken();
		}

		return users;
	}

	public static synchronized void writeUser(ServletConfig config, ArrayList<User> users) {

		String path = FilePaths.getPath(config).getPath();
		System.out.println(path);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

		try {
			PrintWriter wr = new PrintWriter(path + "//userData.txt");
			wr.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		for (User user : users) {
			try {
				String s = ow.writeValueAsString(user);

				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(path + "//userData.txt", true));
					writer.write(s);
					writer.newLine();
					writer.close();
				} catch (Exception e) {
					Response.status(Status.INTERNAL_SERVER_ERROR).build();
				}

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

	}
}
