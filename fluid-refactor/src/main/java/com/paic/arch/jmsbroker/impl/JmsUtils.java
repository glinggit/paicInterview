package com.paic.arch.jmsbroker.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import com.paic.arch.jmsbroker.JmsCallback;
import com.paic.arch.utils.LogUtil;
/**
 * 对jms的封装，给不同的broker调用
 *
 */
public class JmsUtils {
	public static String executeCallbackAgainstConnection(Connection aConnection, String aDestinationName,
			JmsCallback aCallback) {
		Session session = null;
		try {
			session = aConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(aDestinationName);
			return aCallback.performJmsFunction(session, queue);
		} catch (JMSException jmse) {
			LogUtil.ERROR.error("Failed to create session on connection {}", aConnection);
			throw new IllegalStateException(jmse);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException jmse) {
					LogUtil.WARN.warn("Failed to close session {}", session);
					throw new IllegalStateException(jmse);
				}
			}
		}
	}
	
	public static String executeCallbackAgainstRemoteBroker(String aBrokerUrl, String aDestinationName,
			ConnectionFactory connectionFactory, JmsCallback aCallback ) {
		Connection connection = null;
		String returnValue = "";
		try {
			//ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(aBrokerUrl);
			connection = connectionFactory.createConnection();
			connection.start();
			returnValue = JmsUtils.executeCallbackAgainstConnection(connection, aDestinationName, aCallback);
		} catch (JMSException jmse) {
			LogUtil.ERROR.error("failed to create connection to {}", aBrokerUrl);
			throw new IllegalStateException(jmse);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException jmse) {
					LogUtil.WARN.warn("Failed to close connection to broker at []", aBrokerUrl);
					throw new IllegalStateException(jmse);
				}
			}
		}
		return returnValue;
	}
	
	
}
