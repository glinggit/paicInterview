package com.paic.arch.jmsbroker;

import com.paic.arch.jmsbroker.impl.ActiveMqBroker;
import com.paic.arch.jmsbroker.impl.BrokerFactory;

public class JmsMessageBrokerSupportRefactor {
	
	public static ActiveMqBroker bindToActiveMqBrokerAt(String url) {
		//通过工厂方法创建Broker
		return (new BrokerFactory()).createActiveMqBroker(url);
	}
}
