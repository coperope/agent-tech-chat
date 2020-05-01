package serverComunications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Schedule;
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
	private String nodeName = null;
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
    
    @Override
    public List<String> newConnection(String connection){
    	ResteasyClient client = new ResteasyClientBuilder().establishConnectionTimeout(10, TimeUnit.SECONDS)
                .socketTimeout(2, TimeUnit.SECONDS)
                .build();
    	for (String string : this.connection) {
    		ResteasyWebTarget rtarget = client.target("http://" + string + "/WAR2020/rest/server");
    		comunicationsRest rest = rtarget.proxy(comunicationsRest.class);
    		rest.oneNode(connection);
    		
		}
    	ResteasyWebTarget rtarget = client.target("http://" + connection + "/WAR2020/rest/server");
    	comunicationsRest rest = rtarget.proxy(comunicationsRest.class);
		System.out.println(rest.allUsers(this.usrmsg.getUsersLoggedin()));
    	this.connection.add(connection);
    	return this.connection;
    }
    
    @Override
    public boolean oneNode(String connection){
    	for (String string : this.connection) {
			if(string.equals(connection)) {
				return true;
			}
		}
    	this.connection.add(connection);
    	return true;
    }
    
    @Override
    public boolean allNodes(List<String> connection){
    	try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.connection = connection;
    	return true;
    }
    
    @Override
	public boolean allUsers(HashMap<String,User> connection){
		this.usrmsg.setUsersLoggedin(connection);
		return true;
	}
	
    @Override
	public boolean deleteNode(@PathParam("alias") String alias) {
		return true;
	}
	
    @Override
    public String getNode() {
    	return this.nodeName;
    }
    
    @Schedule(hour = "*", minute = "10", info = "every ten minutes")
    public void heartBeat() {
    	ResteasyClient client = new ResteasyClientBuilder().establishConnectionTimeout(100, TimeUnit.SECONDS)
                .socketTimeout(2, TimeUnit.SECONDS)
                .build();
    	for (String string : this.connection) {
    		ResteasyWebTarget rtarget = client.target("http://" + string + "/WAR2020/rest/server");
    		comunicationsRest rest = rtarget.proxy(comunicationsRest.class);
    		String node = rest.getNode();
    		if(node == null) {
    			String node1 = rest.getNode();
    			if (node1 == null) {
    				removeNodeTellEveryone(string);
    			}
    		}
    		
		}
    }
	public void removeNodeTellEveryone(String connection) {
		ResteasyClient client = new ResteasyClientBuilder().establishConnectionTimeout(100, TimeUnit.SECONDS)
                .socketTimeout(2, TimeUnit.SECONDS)
                .build();;
    	for (String string : this.connection) {
    		if(string.equals(connection)) {
    			continue;
    		}
    		ResteasyWebTarget rtarget = client.target("http://" + string + "/WAR2020/rest/server");
    		comunicationsRest rest = rtarget.proxy(comunicationsRest.class);
    		rest.deleteNode(connection);
    	}
    	this.connection.remove(connection);
	}
}
