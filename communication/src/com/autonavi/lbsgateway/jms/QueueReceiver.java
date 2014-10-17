/**
 * 
 */
package com.autonavi.lbsgateway.jms;

/**
 * @author shiguang.zhou
 *
 */
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueReceiver{
 public static void main(String[] args) {

  // ConnectionFactory �����ӹ�����JMS ������������
  ConnectionFactory connectionFactory;
  // Connection ��JMS �ͻ��˵�JMS Provider ������
  Connection connection = null;
  // Session�� һ�����ͻ������Ϣ���߳�
  Session session;
  // Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
  Destination destination;
  // �����ߣ���Ϣ������
  MessageConsumer consumer;

  connectionFactory = new ActiveMQConnectionFactory(
    ActiveMQConnection.DEFAULT_USER,
    ActiveMQConnection.DEFAULT_PASSWORD,
    "tcp://localhost:61616");
  try {
   // ����ӹ����õ����Ӷ���
   connection = connectionFactory.createConnection();
   // ����
   connection.start();
   // ��ȡ��������
   session = connection.createSession(Boolean.FALSE,
     Session.AUTO_ACKNOWLEDGE);
   // ��ȡsessionע�������һ����������queue��������ActiveMq��console����
   destination = session.createQueue("queue");
   consumer = session.createConsumer(destination);
   while (true) {
    TextMessage message = (TextMessage) consumer.receive(1000);
    if (null != message) {
     System.out.println("�յ���Ϣ" + message.getText());

    } else {
     break;
    }

   }

  } catch (Exception e) {
   e.printStackTrace();
  } finally {
   try {
    if (null != connection)
     connection.close();
   } catch (Throwable ignore) {
   }
  }

 }
}


