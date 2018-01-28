package com.paic.arch.jmsbroker.impl;

import com.paic.arch.jmsbroker.IBrokerFactory;

public class BrokerFactory implements IBrokerFactory{

	@Override
	public ActiveMqBroker createActiveMqBroker(String url) {
		
		return new ActiveMqBroker(url);
	}

}
