package com.paic.arch.jmsbroker;

import com.paic.arch.jmsbroker.impl.ActiveMqBroker;

public interface IBrokerFactory {
	public ActiveMqBroker createActiveMqBroker(String url);
}
