package beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Init;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Host;
import model.Message;
import model.User;
import ws.WSEndPoint;

/**
 * Session Bean implementation class UserManagementBean
 */
@Stateless
@LocalBean
@Path("/")
public class UserManagementBean {

	
	
	@Inject
	UsrMsg usrmsg;
    /**
     * Default constructor. 
     */
    public UserManagementBean() {
        // TODO Auto-generated constructor stub
    }
    @POST
	@Path("users/register")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user) {
    	if(usrmsg.usersRegistered.get(user.getUsername()) != null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Already registered").build();
    	}
    	usrmsg.usersRegistered.put(user.getUsername(), user);
		//ws.echoTextMessage(user.toString());
		return Response.status(200).build();
	}
    
    @POST
	@Path("users/login")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response login(User user) {
    	System.out.println("USO");
    	if(usrmsg.usersLoggedin.get(user.getUsername()) != null) {
    		return Response.status(Response.Status.OK).entity("Already logged in").build();
    	}
    	User regUser = usrmsg.usersRegistered.get(user.getUsername());
    	if(regUser == null) {
    		System.out.println("USO1");
    		return Response.status(Response.Status.BAD_REQUEST).entity("Not registered").build();
    	}
    	if(!regUser.getPassword().equals(user.getPassword())){
    		System.out.println("USO2");
    		return Response.status(Response.Status.BAD_REQUEST).entity("Wrong password").build();
    		
    	}
    	usrmsg.usersLoggedin.put(user.getUsername(), regUser);
		//ws.echoTextMessage(regUser.toString());
		return Response.status(200).build();
	}
    
    @DELETE
    @Path("users/loggedIn/{user}")
    public Response logout(@PathParam("user") String user) {
    	if(usrmsg.usersLoggedin.get(user) != null) {
    		usrmsg.usersLoggedin.remove(user);
    		return Response.status(Response.Status.OK).entity("Logged out").build();
    	}
    	return Response.status(Response.Status.BAD_REQUEST).entity("User does not exist").build();
    	
    }
    @GET
    @Path("users/loggedIn")
    public Response loggedInUsers() {
    	System.out.println(usrmsg.toString());
    	Collection<User> usersLoggedIn = (Collection<User>) usrmsg.usersLoggedin.values();
    	return Response.status(Response.Status.OK).entity(usersLoggedIn).build();
    }
    @GET
    @Path("users/registered")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registeredUsers() {
    	System.out.println(usrmsg.toString());
    	Collection<User> usersRegistered = (Collection<User>) usrmsg.usersRegistered.values();
    	
    	return Response.status(Response.Status.OK).entity(usersRegistered).build();
    	
    }
    
    @POST
	@Path("messages/all")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response sendToAll(Message msg) {
    	System.out.println(usrmsg.toString());
    	System.out.println(msg.getContent());
    	if (msg.getSender() == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Sender not found.").build();
    	}
    	User sender = usrmsg.usersRegistered.get(msg.getSender());
    	if (sender == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Sender not registered.").build();
    	}
    	for (Map.Entry<String, User> user : usrmsg.usersRegistered.entrySet()) {
    		if(user.getValue().getUsername().equals(sender.getUsername())) {
    			continue;
    		}
    		User u = user.getValue();
    		msg.setReceiver(u.getUsername());
    		u.receiveMessage(msg);
    		sender.sendedMessage(msg);
    	}
    	
		//ws.echoTextMessage(msg.getContent());
		return Response.status(200).build();
	}
    @POST
	@Path("messages/users")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response sendToUser(Message msg) {
    	System.out.println(msg.getContent());
    	if (msg.getSender() == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Sender not found.").build();
    	}
    	if (msg.getReceiver() == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Receiver not found.").build();
    	}
    	System.out.println(msg.getReceiver()+msg.getSender());
    	System.out.println(usrmsg.toString());
    	User receiver = usrmsg.usersRegistered.get(msg.getReceiver());
    	User sender = usrmsg.usersRegistered.get(msg.getSender());
    	if (receiver == null || sender == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Receiver or sender is not registered.").build();
    	}
    	receiver.receiveMessage(msg);
    	sender.sendedMessage(msg);
		//ws.echoTextMessage(msg.getContent());
		return Response.status(200).build();
	}
    
    @GET
	@Path("messages/{user}")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response getMessages(@PathParam("user") String user) {
    	User u = usrmsg.usersRegistered.get(user);
    	System.out.println(user);
    	System.out.println(u);
    	System.out.println(usrmsg.toString());
    	if (u == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("User not provided or not logged in.").build();
    	}
    	
    	return Response.status(Response.Status.OK).entity(u.getInbox()).build();
		//ws.echoTextMessage(msg.getContent());
		//return Response.status(200).build();
	}

}
