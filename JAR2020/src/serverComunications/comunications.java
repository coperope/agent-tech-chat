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
public class comunications {
	@EJB
	UsrMsg usrmsg;
	
	private String master = "";
	private String nodeName = "1f5acc36.ngrok.io";
	private String nodeAlias = "node2";
	private List<String> connection = new ArrayList<>();
    /**
     * Default constructor. 
     */
    @PostConstruct
	private void init() {
    	System.out.println("Here");
    	if (master != null && !master.equals("")) {
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = client.target("http://" + master + "/WAR2020/rest/server");
			comunicationsRest rest = rtarget.proxy(comunicationsRest.class);
			this.connection = rest.newConnection(this.nodeName);
			this.connection.remove(this.nodeName);
			this.connection.add(this.master);
			this.usrmsg.setUsersLoggedin(rest.getAllUsers());
			System.out.println("ZAVRSIO");
		}
    }
   
    
    @Schedule(hour = "*", minute = "*",second = "*/45", info = "every ten minutes")
    public void heartBeat() {
    	ResteasyClient client = new ResteasyClientBuilder()
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
		ResteasyClient client = new ResteasyClientBuilder()
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


	public String getNodeName() {
		return nodeName;
	}


	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeAlias() {
		return nodeAlias;
	}


	public void setNodeAlias(String nodeAlias) {
		this.nodeAlias = nodeAlias;
	}

	public List<String> getConnection() {
		return connection;
	}


	public void setConnection(List<String> connection) {
		this.connection = connection;
	}
	
}
