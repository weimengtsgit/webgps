package com.autonavi.lbsgateway.jms;

import java.io.IOException;
import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.IndentPrinter;

import com.autonavi.directl.Config;

 
 

public class ProduceTopicServer {

	private Destination destination;
	 
	private String user = "";

	private String password = "";

	/*private String url = "tcp://172.17.20.138:61616";*/

	private String subject = "GPS.REQUEST";

	private boolean topic;
 	
	private String Message="JMS测试";

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public void sendJMSMsg() {
		Connection connection = null;
		try {
			 
			// Create the connection.
			Config config = new Config();
			String jmsip = config.getString("mqIpPort");
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					user, password, jmsip);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			 
			destination = session.createTopic(subject);
			// Create the producer.
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
 
			// Start sending messages
			sendLoop(session, producer);
 			// Use the ActiveMQConnection interface to dump the connection
			 
			ActiveMQConnection c = (ActiveMQConnection) connection;
			c.getConnectionStats().dump(new IndentPrinter());

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (Throwable ignore) {
			}
		}
	}
/**
 * 发送页面信息
 * @param session
 * @param producer
 * @throws Exception
 */
	protected void sendLoop(Session session, MessageProducer producer)
			throws Exception {

		for (int i = 0; i < 5; i++) {

			//TextMessage message = session.createTextMessage(this.getMessage());
			String messages="JMS测试"+i;
 		 
			BytesMessage message=  session.createBytesMessage(); 
            message.writeBytes(messages.getBytes());
  
			producer.send(message);
			 
 
 
		}

	}
    

	 

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTopic(boolean topic) {
		this.topic = topic;
	}

	public void setQueue(boolean queue) {
		this.topic = !queue;
	}

	 

	public void setUser(String user) {
		this.user = user;
	}	 
	
	public static void main(String[] args){
		ProduceTopicServer ps = new ProduceTopicServer();
		ps.sendJMSMsg();
	}
}
