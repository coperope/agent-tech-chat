package ws;

import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import beans.UsrMsg;
import model.User;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/mojQueue")
})
public class QueueMDB implements MessageListener {
	@EJB WSEndPoint ws;
	
	@EJB
	UsrMsg agentRepo;
	
	@Override
	public void onMessage(Message msg) {
		try {
			ObjectMessage objectMessage = (ObjectMessage) msg;
	        model.Message message = (model.Message) objectMessage.getObject();
	        User sender = agentRepo.getUsersRegistered().get(message.getSender());
			if(message.getReceiver() == null) {
				for (Map.Entry<String, User> user : agentRepo.getUsersRegistered().entrySet()) {
					if(user.getValue().getUsername().equals(sender.getUsername())) {
		    			continue;
		    		}
					User u = user.getValue();
		    		message.setReceiver(u.getUsername());
		    		u.handleMessage(message);
		    		sender.sendedMessage(message);
		    	}
			}else {
				User receiver = agentRepo.getUsersRegistered().get(message.getReceiver());
				if (receiver != null) {
					receiver.receiveMessage(message);
				}
				if (sender != null) {
					sender.sendedMessage(message);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
