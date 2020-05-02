package serverComunications;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import beans.UsrMsg;
import model.User;

/**
 * Session Bean implementation class ConnectionsBean
 */
@Stateless
@LocalBean
@Path("/server")
@Remote(comunicationsRest.class)
public class ConnectionsBean implements comunicationsRest {

	@EJB
	UsrMsg usrmsg;
	
	@EJB
	comunications comunications;
    /**
     * Default constructor. 
     */
    public ConnectionsBean() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<String> newConnection(String connection){
    	ResteasyClient client = new ResteasyClientBuilder()
                .build();
    	for (String string : comunications.getConnection()) {
    		ResteasyWebTarget rtarget = client.target("http://" + string + "/WAR2020/rest/server");
    		comunicationsRest rest = rtarget.proxy(comunicationsRest.class);
    		rest.allUsers(usrmsg.getUsersLoggedin());
    		
		}
    	ResteasyWebTarget rtarget = client.target("http://" + connection + "/WAR2020/rest/server");
    	//rest = rtarget.proxy(comunicationsRest.class);
		//boolean test = rest.allUsers(this.usrmsg.getUsersLoggedin());
		//System.out.println(connection);
    	
		comunications.getConnection().add(connection);
    	return comunications.getConnection();
    }
    
    @Override
    public boolean oneNode(String connection){
    	for (String string : comunications.getConnection()) {
			if(string.equals(connection)) {
				return true;
			}
		}
    	comunications.getConnection().add(connection);
    	return true;
    }
    
    @Override
    public boolean allNodes(List<String> connection){
    	comunications.setConnection(connection);
    	return true;
    }
    
    @Override
	public boolean allUsers(HashMap<String,User> connection){
		this.usrmsg.setUsersLoggedin(connection);
		return true;
	}
    
    @Override
    public HashMap<String,User> getAllUsers(){
		return this.usrmsg.getUsersLoggedin();
    }
	
    @Override
	public boolean deleteNode(@PathParam("alias") String alias) {
		return true;
	}
	
    @Override
    public String getNode() {
    	return comunications.getNodeName();
    }
}
