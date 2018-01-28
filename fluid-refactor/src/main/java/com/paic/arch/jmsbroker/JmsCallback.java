package com.paic.arch.jmsbroker;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

public interface JmsCallback {
	String performJmsFunction(Session aSession, Destination aDestination) throws JMSException;
	
}
