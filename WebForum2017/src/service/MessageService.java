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

import file.MessageFileController;
import file.UserFileController;
import model.Message;
import model.User;

@Path("messages")
public class MessageService {

	@Context
	ServletConfig config;

	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String sendMessage(Message m) throws FileNotFoundException, IOException {
		System.out.println("usao sam u sendMessage preko RESTa");
		ArrayList<Message> listOfMessage = MessageFileController.readMessage(config);
		ArrayList<User> listOfUser = UserFileController.readUser(config);

		for (User user : listOfUser) {
			if (user.getUsername().equals(m.getReceiver())) {
				listOfMessage.add(m);
				MessageFileController.writeMessage(config, listOfMessage);
				return "Message sent";
			}
		}
		return "User with this username not exist";

	}

	@GET
	@Path("/getMessage/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> getMessage(@PathParam(value = "username") String username)
			throws FileNotFoundException, IOException {
		System.out.println("nesto " + username);
		ArrayList<Message> listOfMessage = MessageFileController.readMessage(config);
		ArrayList<Message> newListOfMessage = new ArrayList<>();

		for (Message m : listOfMessage) {
			if (m.getReceiver().equals(username)) {
				newListOfMessage.add(m);
			}
		}

		return newListOfMessage;
	}

	@POST
	@Path("/readMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String readMessage(Message message) throws FileNotFoundException, IOException {
		ArrayList<Message> listOfMessage = MessageFileController.readMessage(config);
		for (Message m : listOfMessage) {
			if (m.getSender().equals(message.getSender()) && m.getContent().equals(message.getContent())) {
				m.setSeen(true);
			}

			MessageFileController.writeMessage(config, listOfMessage);
		}
		return "Message seen";
	}

}
