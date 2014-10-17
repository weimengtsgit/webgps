/**
 * 
 */
package com.autonavi.lbsgateway.jms;

/**
 * @author shiguang.zhou
 *
 */
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;


public class TopicTest {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://172.17.40.55:61616");
   
        Connection connection = factory.createConnection();
        connection.start();
       
        //����һ��Topic
        Topic topic= new ActiveMQTopic("abcd");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        //ע��������1
        MessageConsumer comsumer1 = session.createConsumer(topic);
        comsumer1.setMessageListener(new MessageListener(){
            public void onMessage(Message m) {
                try {
                    System.out.println("GPS ��Ϣ�� " + ((TextMessage)m).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
       
        
//        //����һ�������ߣ�Ȼ���Ͷ����Ϣ��
//        MessageProducer producer = session.createProducer(topic);
//        for(int i=0; i<10; i++){
//            producer.send(session.createTextMessage("Message:" + i));
//        }
    }

}