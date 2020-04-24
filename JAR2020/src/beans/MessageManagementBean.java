package beans;

import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Message;
import model.User;
import ws.WSEndPoint;

@Stateless
@LocalBean
@Path("/messages")
public class MessageManagementBean {

	@EJB
	WSEndPoint ws;
	
	@EJB
	UsrMsg usrmsg;
	
    @POST
	@Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response sendToAll(Message msg) {
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
    		u.receiveMessage(msg);
    	}
    	
		ws.echoTextMessage(msg.getContent());
		return Response.status(200).build();
	}
    @POST
	@Path("/users")
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
    	User receiver = usrmsg.usersRegistered.get(msg.getReceiver());
    	User sender = usrmsg.usersRegistered.get(msg.getSender());
    	if (receiver == null || sender == null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("Receiver or sender is not registered.").build();
    	}
    	receiver.receiveMessage(msg);
    	sender.sendedMessage(msg);
		ws.echoTextMessage(msg.getContent());
		return Response.status(200).build();
	}
}
