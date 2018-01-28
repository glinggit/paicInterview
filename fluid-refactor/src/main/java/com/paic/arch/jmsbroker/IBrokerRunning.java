package com.paic.arch.jmsbroker;

public interface IBrokerRunning {
	
	public IBrokerRunning sendATextMessageToDestinationAt(String aDestinationName, String aMessageToSend);
	
	public String retrieveASingleMessageFromTheDestination(String aDestinationName);
	
	public String retrieveASingleMessageFromTheDestination(String aDestinationName, int aTimeout);

	public long getEnqueuedMessageCountAt(String aDestinationName) throws Exception;

    public boolean isEmptyQueueAt(String aDestinationName) throws Exception;
    
    public IBrokerRunning andThen();
    
    public String getBrokerUrl();
    
//    public IBrokerRunning sendTheMessage(String inputMessage);
//    
//    public IBrokerRunning to(String inputQueue);

}
