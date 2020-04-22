package beans;

import java.util.HashMap;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
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

}
