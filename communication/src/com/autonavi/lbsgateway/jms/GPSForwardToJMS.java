/**
 * 
 */
package com.autonavi.lbsgateway.jms;

import java.util.ArrayList;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import com.autonavi.directl.Config;
import com.autonavi.directl.Log;
import com.autonavi.directl.parse.ParseBase;

/**
 * @author shiguang.zhou
 * 把GPS信息转发到JMS
 */
public class GPSForwardToJMS {
	
	private boolean transacted;

	private long sleepTime = 1000;
	
	public void sendMessage(String subject, ArrayList pbList){
		
		Config config = new Config();
		String jmsip = config.getString("mqIpPort");
		
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(jmsip);
		   
        Connection connection = null;
		try {
			connection = factory.createConnection();
			connection.start();
			 //创建一个Topic
	        Topic topic= new ActiveMQTopic(subject);
	        Session session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
	        
	        //创建一个生产者，然后发送多个消息。
	        MessageProducer producer = session.createProducer(topic);
	        StringBuffer sbuf = new StringBuffer();
	        sbuf.append(pbList.size()+",");
	        
	        for(int i=0; i<pbList.size(); i++){
	        	ParseBase pb = (ParseBase)pbList.get(i);
	        	sbuf.append(pb.makeJMSInfo());
	        	if (i!=pbList.size()-1){
	        		sbuf.append(",");
	        	}	            
	        }
	        BytesMessage  message = session.createBytesMessage();
	        message.writeBytes(sbuf.toString().getBytes());
	        message.setStringProperty("SrvId","Topic://abcd");
	        message.setStringProperty("Uid","Mapabc");
           
	        producer.send(message);
	        if (transacted)
	            session.commit();
	        Log.getInstance().outJHSLoger("位置信息转发到JMS:"+sbuf.toString());
	        Thread.sleep(sleepTime);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			Log.getInstance().outJHSLoger("JMS发送异常："+e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.getInstance().outJHSLoger("JMS发送异常："+e.getMessage());
			e.printStackTrace();
		}finally {
			try {
				connection.close();
			} catch (Throwable ignore) {
			}
		}
        
       
       
	}
}
