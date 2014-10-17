/**
 * 
 */
package com.autonavi.lbsgateway.jms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
//·¢ËÍBytesMessage
public class SendByteMessage {
    
    private String url = "tcp://localhost:61616";
        
    public void sendMessage() throws JMSException{
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session  session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("test.queue");
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        BytesMessage message = session.createBytesMessage();
        byte[] content = getFileByte("d://test.jar");
        message.writeBytes(content);
        try{
            producer.send(message);
            System.out.println("successful send message");
        }catch(Exception e){
            e.printStackTrace();
            e.getMessage();
        }finally{
            session.close();
            connection.close();
        }                
    }
    
    private byte[] getFileByte(String filename){
        byte[] buffer = null;
        FileInputStream fin = null;
        try {
            File file = new File(filename);
            fin = new FileInputStream(file); 
            buffer = new byte[fin.available()];
            fin.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }
}