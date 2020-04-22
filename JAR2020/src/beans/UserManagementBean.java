package beans;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
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
import model.User;
import ws.WSEndPoint;

/**
 * Session Bean implementation class UserManagementBean
 */
@Stateless
@LocalBean
@Path("/users")
public class UserManagementBean {

	@EJB
	WSEndPoint ws;
	
	@EJB
	UsrMsg usrmsg;
    /**
     * Default constructor. 
     */
    public UserManagementBean() {
        // TODO Auto-generated constructor stub
    }
    @POST
	@Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user) {
    	if(usrmsg.usersRegistered.get(user.getUsername()) != null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Already registered").build();
    	}
    	usrmsg.usersRegistered.put(user.getUsername(), user);
		ws.echoTextMessage(user.toString());
		return Response.status(200).build();
	}
    
    @POST
	@Path("/login")
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
    	usrmsg.usersLoggedin.put(user.getUsername(), user);
		ws.echoTextMessage(user.toString());
		return Response.status(200).build();
	}
    
    @DELETE
    @Path("/loggedIn/{user}")
    public Response logout(@PathParam("user") String user) {
    	if(usrmsg.usersLoggedin.get(user) != null) {
    		usrmsg.usersLoggedin.remove(user);
    		return Response.status(Response.Status.OK).entity("Logged out").build();
    	}
    	return Response.status(Response.Status.BAD_REQUEST).entity("User does not exist").build();
    	
    }
    @GET
    @Path("/loggedIn")
    public Response loggedInUsers() {
    	Collection<User> usersLoggedIn = (Collection<User>) usrmsg.usersLoggedin.values();
    	return Response.status(Response.Status.OK).entity(usersLoggedIn).build();
    }
    @GET
    @Path("/registered")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registeredUsers() {
    	Collection<User> usersRegistered = (Collection<User>) usrmsg.usersRegistered.values();
    	
    	return Response.status(Response.Status.OK).entity(usersRegistered).build();
    	
    	
    }
}
