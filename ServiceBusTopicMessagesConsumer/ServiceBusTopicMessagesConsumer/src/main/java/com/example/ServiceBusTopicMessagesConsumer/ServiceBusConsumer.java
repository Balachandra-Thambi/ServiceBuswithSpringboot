package com.example.ServiceBusTopicMessagesConsumer;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.IQueueClient;
import com.microsoft.azure.servicebus.ISubscriptionClient;
import com.microsoft.azure.servicebus.ITopicClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

import lombok.extern.log4j.Log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Component
class ServiceBusConsumer implements Ordered {

    //private ISubscriptionClient iSubscriptionClient1 ;
    //@Autowired
    private IQueueClient iqueue;
	
	  //private ISubscriptionClient iSubscriptionClient2 ; private
	  //ISubscriptionClient iSubscriptionClient3 ; 
    private final Logger log = LoggerFactory.getLogger(ServiceBusConsumer.class); 
   // private String connectionString ="Endpoint=sb://topicsinservicebus.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=ny3fKCHs2eFllaAkmT4VUDw5F+r815o1P2ftwOhZLhI=";
	 
    ServiceBusConsumer(IQueueClient iq) {
		this.iqueue = iq;
	}
    
   
    
    
	@EventListener(ApplicationReadyEvent.class)
    public void consume() throws Exception {

    	recievingmessages(iqueue);
    	//recievingmessages(iSubscriptionClient2);
    	//recievingmessages(iSubscriptionClient3);
    	    	
    }

    @SuppressWarnings("deprecation")
	public void recievingmessages(IQueueClient iqueueclient) throws InterruptedException, ServiceBusException {


    	iqueueclient.registerMessageHandler(new IMessageHandler() {

            @Override
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
                log.info("received message " + new String(message.getBody()) + " with body ID " + message.getMessageId());
                return CompletableFuture.completedFuture(null);
            }
            
            @Override
            public void notifyException(Throwable exception, ExceptionPhase phase) {
                log.error("eeks!", exception);
            }
        });
        
    
    	
    }
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
