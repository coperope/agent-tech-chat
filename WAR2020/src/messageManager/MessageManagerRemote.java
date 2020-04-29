package messageManager;

import javax.ejb.Remote;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import model.Message;

@Remote
public interface MessageManagerRemote {
	public void post(Message msg);
}
