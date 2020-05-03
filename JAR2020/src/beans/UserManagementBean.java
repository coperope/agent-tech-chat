package beans;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import messageManager.MessageManagerBean;
import model.Host;
import model.Message;
import model.User;
import serverComunications.ConnectionsBean;
import serverComunications.comunications;
import serverComunications.comunicationsRest;
import ws.WSEndPoint;

/**
 * Session Bean implementation class UserManagementBean
 */
@Stateless
@LocalBean
@Path("/")
public class UserManagementBean {

	@EJB
	WSEndPoint ws;
	
	@Inject
	UsrMsg usrmsg;
	
	@Inject
	ConnectionsBean communicate;
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/mojQueue")
	private Queue queue;
	
	@EJB
	MessageManagerBean msm;
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
    	if(usrmsg.getUsersRegistered().get(user.getUsername()) != null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Already registered").build();
    	}
    	user.setHost(communicate.getHost());
    	usrmsg.getUsersRegistered().put(user.getUsername(), user);
		//ws.echoTextMessage(user.toString());
		return Response.status(200).build();
	}
    
    @POST
	@Path("users/login")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response login(User user) {
    	System.out.println("USO");
    	if(usrmsg.getUsersLoggedin().get(user.getUsername()) != null) {
    		return Response.status(Response.Status.OK).entity("Already logged in").build();
    	}
    	User regUser = usrmsg.getUsersRegistered().get(user.getUsername());
    	if(regUser == null) {
    		System.out.println("USO1");
    		return Response.status(Response.Status.BAD_REQUEST).entity("Not registered").build();
    	}
    	if(!regUser.getPassword().equals(user.getPassword())){
    		System.out.println("USO2");
    		return Response.status(Response.Status.BAD_REQUEST).entity("Wrong password").build();
    		
    	}
    
    	usrmsg.getUsersLoggedin().put(user.getUsername(), regUser);
    	ResteasyClient client = new ResteasyClientBuilder()
                .build();
    	communicate.tellEveryone(usrmsg.getUsersLoggedin());
    	Collection<User> usersLoggedIn = (Collection<User>) usrmsg.getUsersLoggedin().values();
    	Gson gson = new Gson();
    	String loggedIn = gson.toJson(usersLoggedIn); 
    	System.out.println(loggedIn);
		ws.echoTextMessage(loggedIn);
		return Response.status(200).build();
	}
    
    @DELETE
    @Path("users/loggedIn/{user}")
    public Response logout(@PathParam("user") String user) {
    	if(usrmsg.getUsersLoggedin().get(user) != null) {
    		usrmsg.getUsersLoggedin().remove(user);
    		Collection<User> usersLoggedIn = (Collection<User>) usrmsg.usersLoggedin.values();
        	Gson gson = new Gson();
        	String loggedIn = gson.toJson(usersLoggedIn); 
    		ws.echoTextMessage(loggedIn);
    		return Response.status(Response.Status.NO_CONTENT).entity("Logged out").build();
    	}else {
    		return Response.status(Response.Status.BAD_REQUEST).entity("User does not exist").build();
    	}
    	
    	
    }
    @GET
    @Path("users/loggedIn")
    public Response loggedInUsers() {
    	Collection<User> usersLoggedIn = (Collection<User>) usrmsg.getUsersLoggedin().values();
    	return Response.status(Response.Status.OK).entity(usersLoggedIn).build();
    }
    @GET
    @Path("users/registered")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registeredUsers() {
    	System.out.println(usrmsg.toString());
    	Collection<User> usersRegistered = (Collection<User>) usrmsg.getUsersRegistered().values();
    	
    	return Response.status(Response.Status.OK).entity(usersRegistered).build();
    	
    }
    
    @POST
	@Path("messages/all")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response sendToAll(Message msg) {
    	System.out.println(msg.getContent());
    	if (msg.getSender() == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Sender not found.").build();
    	}
    	User sender = usrmsg.getUsersRegistered().get(msg.getSender());
    	if (sender == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Sender not registered.").build();
    	}
    	msm.post(msg);
    	
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
    	User receiver = usrmsg.getUsersRegistered().get(msg.getReceiver());
    	User sender = usrmsg.getUsersRegistered().get(msg.getSender());
    	if (receiver == null || sender == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Receiver or sender is not registered.").build();
    	}
    	msm.post(msg);
		//ws.echoTextMessage(msg.getContent());
		return Response.status(200).build();
	}
    
    @GET
	@Path("messages/{user}")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response getMessages(@PathParam("user") String user) {
    	User u = usrmsg.getUsersRegistered().get(user);
    	System.out.println(usrmsg.toString());
    	if (u == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("User not provided or not logged in.").build();
    	}
    	System.out.println(u.getInbox());
    	return Response.status(Response.Status.OK).entity(u.getInbox()).build();
		//ws.echoTextMessage(msg.getContent());
		//return Response.status(200).build();
	}

}
