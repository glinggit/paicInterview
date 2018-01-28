package com.paic.arch.jmsbroker.impl;

import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.DestinationStatistics;

import com.paic.arch.exception.NoMessageReceivedException;
import com.paic.arch.jmsbroker.IBrokerCreate;
import com.paic.arch.jmsbroker.IBrokerRunning;
import com.paic.arch.utils.LogUtil;
import com.paic.arch.utils.SocketFinder;

/**
 * IBrokerRunning,IBrokerCreate 拆分的依据是单一原则，一个负责broker的创建，一个负责数据的发送和接收
 *
 */
public class ActiveMqBroker implements IBrokerRunning, IBrokerCreate {

	private static final int ONE_SECOND = 1000;
	private static final int DEFAULT_RECEIVE_TIMEOUT = 10 * ONE_SECOND;
	public static final String DEFAULT_BROKER_URL_PREFIX = "tcp://localhost:";

	private String brokerUrl;
	private BrokerService brokerService;
	
	private String inputMessage;

	public ActiveMqBroker() {
	}

	public ActiveMqBroker(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public IBrokerCreate createARunningEmbeddedBrokerOnAvailablePort() throws Exception {
		return createARunningEmbeddedBrokerAt(
				DEFAULT_BROKER_URL_PREFIX + SocketFinder.findNextAvailablePortBetween(41616, 50000));
	}

	public static IBrokerCreate createARunningEmbeddedBrokerAt(String aBrokerUrl) throws Exception {
		LogUtil.DEBUG.debug("Creating a new broker at {}", aBrokerUrl);
		ActiveMqBroker broker = new ActiveMqBroker(aBrokerUrl);
		broker.createEmbeddedBroker();
		broker.startEmbeddedBroker();
		return broker;
	}

	private void createEmbeddedBroker() throws Exception {
		brokerService = new BrokerService();
		brokerService.setPersistent(false);
		brokerService.addConnector(brokerUrl);
	}

	private void startEmbeddedBroker() throws Exception {
		brokerService.start();
	}

	@Override
	public void stopTheRunningBroker() throws Exception {
		if (brokerService == null) {
			throw new IllegalStateException("Cannot stop the broker from this API: "
					+ "perhaps it was started independently from this utility");
		}
		brokerService.stop();
		brokerService.waitUntilStopped();

	}

	@Override
	public IBrokerRunning sendATextMessageToDestinationAt(String aDestinationName, String aMessageToSend) {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		JmsUtils.executeCallbackAgainstRemoteBroker(brokerUrl, aDestinationName,connectionFactory, (aSession, aDestination) -> {
			MessageProducer producer = aSession.createProducer(aDestination);
			producer.send(aSession.createTextMessage(aMessageToSend));
			producer.close();
			return "";
		});
		return this;
	}

	@Override
	public String retrieveASingleMessageFromTheDestination(String aDestinationName) {
		return retrieveASingleMessageFromTheDestination(aDestinationName, DEFAULT_RECEIVE_TIMEOUT);
	}

	@Override
	public String retrieveASingleMessageFromTheDestination(String aDestinationName, int aTimeout) {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		return JmsUtils.executeCallbackAgainstRemoteBroker(brokerUrl, aDestinationName, connectionFactory, (aSession, aDestination) -> {
			MessageConsumer consumer = aSession.createConsumer(aDestination);
			Message message = consumer.receive(aTimeout);
			if (message == null) {
				throw new NoMessageReceivedException(
						String.format("No messages received from the broker within the %d timeout", aTimeout));
			}
			consumer.close();
			return ((TextMessage) message).getText();
		});
	}

	@Override
	public long getEnqueuedMessageCountAt(String aDestinationName) throws Exception {
		return getDestinationStatisticsFor(aDestinationName).getMessages().getCount();
	}

	// 获取Destination
	private DestinationStatistics getDestinationStatisticsFor(String aDestinationName) throws Exception {
		Broker regionBroker = brokerService.getRegionBroker();
		for (org.apache.activemq.broker.region.Destination destination : regionBroker.getDestinationMap().values()) {
			if (destination.getName().equals(aDestinationName)) {
				return destination.getDestinationStatistics();
			}
		}
		throw new IllegalStateException(
				String.format("Destination %s does not exist on broker at %s", aDestinationName, brokerUrl));
	}

	@Override
	public boolean isEmptyQueueAt(String aDestinationName) throws Exception {
		return getEnqueuedMessageCountAt(aDestinationName) == 0;
	}

	public final IBrokerRunning andThen() {
		return this;
	}

	public final String getBrokerUrl() {
		return brokerUrl;
	}

//	@Override
//	public IBrokerRunning sendTheMessage(String inputMessage) {
//		this.inputMessage = inputMessage;
//		return this;
//	}
//
//	@Override
//	public IBrokerRunning to(String inputQueue) {
//		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
//		JmsUtils.executeCallbackAgainstRemoteBroker(brokerUrl, inputQueue,connectionFactory, (aSession, aDestination) -> {
//			MessageProducer producer = aSession.createProducer(aDestination);
//			producer.send(aSession.createTextMessage(inputMessage));
//			producer.close();
//			return "";
//		});
//		return this;
//	}

}
