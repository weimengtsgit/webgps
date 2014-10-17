/**
 * 
 */
package com.autonavi.lbsgateway.jms;

/**
 * @author shiguang.zhou
 *
 */
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


public class ReceiveMessage {
    
    private static final String url = "tcp://172.16.168.167:61616";
    private static final String QUEUE_NAME = "szf.queue";

    
    public void receiveMessage(){
        Connection connection = null;
        try{
            try{
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                connection = connectionFactory.createConnection();
            }catch(Exception e){
//                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
//                connection = connectionFactory.createConnection();
            }            
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE_NAME);
            MessageConsumer consumer = session.createConsumer(destination);
            consumeMessagesAndClose(connection,session,consumer);
        }catch(Exception e){
            
        }
    }
    
    protected void consumeMessagesAndClose(Connection connection,
            Session session, MessageConsumer consumer) throws JMSException {
        for (int i = 0; i < 1;) {
            Message message = consumer.receive(1000);
            if (message != null) {
                i++;
                onMessage(message);
            }
        }
        System.out.println("Closing connection");
        consumer.close();
        session.close();
        connection.close();
    }
    
    public void onMessage(Message message){
        try{
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage)message;
                String msg = txtMsg.getText();
                System.out.println("Received: " + msg);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    public static void main(String args[]){
        ReceiveMessage rm = new ReceiveMessage();
        rm.receiveMessage();
    }
}