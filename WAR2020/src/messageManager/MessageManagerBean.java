package messageManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

/**
 * Session Bean implementation class MessageManagerBean
 */
@Stateless
@LocalBean
public class MessageManagerBean implements MessageManagerRemote {

	/**
	 * Default constructor.
	 */
	public MessageManagerBean() {
	}

	@EJB
	private JMSFactory factory;

	private QueueSession session;
	private QueueSender defaultProducer;

	@PostConstruct
	public void postConstruct() {
		session = factory.getSession();
		defaultProducer = factory.getProducer(session);
	}

	@PreDestroy
	public void preDestroy() {
		try {
			session.close();
		} catch (JMSException e) {
		}
	}

	@Override
	public void post(model.Message msg) {
		try {
			ObjectMessage message = session.createObjectMessage();
			message.setObject(msg);
			defaultProducer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
