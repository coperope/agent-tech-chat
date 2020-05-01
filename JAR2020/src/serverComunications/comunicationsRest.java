package serverComunications;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.User;

public interface comunicationsRest {
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<String> newConnection(String connection);
	
	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean oneNode(String connection);
	
	@POST
	@Path("/nodes")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean allNodes(List<String> connection);
	
	@POST
	@Path("/users/loggedIn")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean allUsers(HashMap<String,User> connection);
	
	@GET
	@Path("/users/loggedIn")
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<String,User> getAllUsers();
	
	@DELETE
	@Path("/node/{alias}")
	public boolean deleteNode(@PathParam("alias") String alias);
	
	@GET
	@Path("/node")
	public String getNode();
}
