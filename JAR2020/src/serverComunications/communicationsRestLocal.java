package serverComunications;

import java.util.HashMap;

import model.Host;
import model.User;

public interface communicationsRestLocal {
	public void tellEveryone(HashMap<String,User> usersLoggedIn);

	public Host getHost();
	

}
