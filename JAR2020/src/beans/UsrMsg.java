package beans;

import java.util.HashMap;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import model.User;
/**
 * Session Bean implementation class UsrMsg
 */
@Singleton
@LocalBean
public class UsrMsg {
	public HashMap<String, User> usersRegistered;
	public HashMap<String, User> usersLoggedin;
    /**
     * Default constructor. 
     */
    public UsrMsg() {
        // TODO Auto-generated constructor stub
    	usersRegistered = new HashMap<String, User>();
    	usersLoggedin = new HashMap<String, User>();
    }
    
    
    
    public HashMap<String, User> getUsersRegistered() {
		return usersRegistered;
	}



	public void setUsersRegistered(HashMap<String, User> usersRegistered) {
		this.usersRegistered = usersRegistered;
	}



	public HashMap<String, User> getUsersLoggedin() {
		return usersLoggedin;
	}



	public void setUsersLoggedin(HashMap<String, User> usersLoggedin) {
		this.usersLoggedin = usersLoggedin;
	}



	@Override
    public String toString() {
    	// TODO Auto-generated method stub
    	String retVal = "";
    	for (User user : this.usersRegistered.values()) {
    		retVal += user.toString() + "\n";
		}
    	return retVal;
    }

}
