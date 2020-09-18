package com.example.SBTopicFilterConsumerApplication;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.ISubscriptionClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

import lombok.extern.log4j.Log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Component
class ServiceBusConsumer {

    private ISubscriptionClient iSubscriptionClient1 ;
    private ISubscriptionClient iSubscriptionClient2 ;
    private ISubscriptionClient iSubscriptionClient3 ;
    private ISubscriptionClient iSubscriptionClient4 ;
    private final Logger log = LoggerFactory.getLogger(ServiceBusConsumer.class);
    private String connectionString = "Endpoint=sb://topicsinservicebus.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=ny3fKCHs2eFllaAkmT4VUDw5F+r815o1P2ftwOhZLhI=";
	
    ServiceBusConsumer(){
    try {
		iSubscriptionClient1 = new SubscriptionClient(new ConnectionStringBuilder(connectionString,"topicfiltersampletopic/subscriptions/AllOrders"), ReceiveMode.PEEKLOCK);
		
		iSubscriptionClient2 = new SubscriptionClient(new ConnectionStringBuilder(connectionString,"topicfiltersampletopic/subscriptions/ColorBlue"), ReceiveMode.PEEKLOCK);
		iSubscriptionClient3 = new SubscriptionClient(new ConnectionStringBuilder(connectionString,"topicfiltersampletopic/subscriptions/ColorRed"), ReceiveMode.PEEKLOCK);
		iSubscriptionClient4 = new SubscriptionClient(new ConnectionStringBuilder(connectionString,"topicfiltersampletopic/subscriptions/HighPriorityOrdersWithRedColor"), ReceiveMode.PEEKLOCK);

		
	} catch (InterruptedException | ServiceBusException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     }
   
    
    
	@EventListener(ApplicationReadyEvent.class)
    public void consume() throws Exception {

    	recievingmessages(iSubscriptionClient1);
    	recievingmessages(iSubscriptionClient2);
    	recievingmessages(iSubscriptionClient3);
    	recievingmessages(iSubscriptionClient4);
    }

    @SuppressWarnings("deprecation")
	public void recievingmessages(ISubscriptionClient iSubscriptionClient) throws InterruptedException, ServiceBusException {


        iSubscriptionClient.registerMessageHandler(new IMessageHandler() {

            @Override
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
                log.info("received message " + new String(message.getBody()) + " with body ID " + "for Subscritpion - " + iSubscriptionClient.getEntityPath());
                return CompletableFuture.completedFuture(null);
            }
            
            @Override
            public void notifyException(Throwable exception, ExceptionPhase phase) {
                log.error("eeks!", exception);
            }
        });
        
    
    	
    }
   
}
