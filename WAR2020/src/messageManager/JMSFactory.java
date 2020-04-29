package messageManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * Session Bean implementation class JMSFactory
 */
@Singleton
@LocalBean
public class JMSFactory {

	private QueueConnection connection;
	@Resource(mappedName  = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName  = "java:jboss/exported/jms/queue/mojQueue")
	private Queue defaultQueue;

	@PostConstruct
	public void postConstruction() {
		try {
			connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			connection.start();
		} catch (JMSException ex) {
			throw new IllegalStateException(ex);
		} 
	}

	@PreDestroy
	public void preDestroy() {
		try {
			connection.close();
		} catch (JMSException ex) {
			System.out.println("Exception while closing the JMS connection." + ex);
		}
	}

	public QueueSession getSession() {
		try {
			return connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		} catch (JMSException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public QueueSender getProducer(QueueSession session) {
		try {
			return session.createSender(defaultQueue);
		} catch (JMSException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
