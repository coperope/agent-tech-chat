package serverComunications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import beans.UsrMsg;
import model.User;
/**
 * Session Bean implementation class comunications
 */
@Singleton
@Startup
@Remote(comunicationsRest.class)
@Path("/server")
public class comunications implements comunicationsRest{

	@EJB
	UsrMsg usrmsg;
	private String master = null;
	private String nodeAddr;
	private String nodeName;
	private List<String> connection = new ArrayList<>();;
    /**
     * Default constructor. 
     */
    @PostConstruct
	private void init() {
    	if (master != null && !master.equals("")) {
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = client.target("http://" + master + "/WAR2020/rest/server");
			comunicationsRest rest = rtarget.proxy(comunicationsRest.class);
			this.connection = rest.newConnection(this.nodeName);
			this.connection.remove(this.nodeName);
			this.connection.add(this.master);
		}
    }
    public List<String> newConnection(String connection){
    	return this.connection;
    }
    public void oneNode(String connection){
    	
    }
    
    public List<String> allNodes(String connection){
    	return this.connection;
    }
    
	public Collection<User> allUsers(String connection){
		Collection<User> usersLoggedIn = (Collection<User>) usrmsg.getUsersLoggedin().values();
    	return usersLoggedIn;
	}
	
	public boolean deleteNode(@PathParam("alias") String alias) {
		return true;
	}
	
	
}
