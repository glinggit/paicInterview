package com.paic.arch.jmsbroker;

public interface IBrokerCreate {
//	public static final String DEFAULT_BROKER_URL_PREFIX = "tcp://localhost:";

	public IBrokerCreate createARunningEmbeddedBrokerOnAvailablePort() throws Exception;

	public void stopTheRunningBroker() throws Exception;

//	static IBrokerCreate createConnection(Object service) {
//		String url = DEFAULT_BROKER_URL_PREFIX + SocketFinder.findNextAvailablePortBetween(41616, 50000);
//	
//		ActiveMqBroker broker = new ActiveMqBroker(url);
//		broker.createEmbeddedBroker();
//		broker.startEmbeddedBroker();
//		return broker;
//	
//	
//	
//	}
}
