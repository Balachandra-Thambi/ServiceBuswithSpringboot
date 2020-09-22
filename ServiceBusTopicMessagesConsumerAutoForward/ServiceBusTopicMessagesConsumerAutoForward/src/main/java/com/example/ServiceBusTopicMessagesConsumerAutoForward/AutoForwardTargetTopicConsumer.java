package com.example.ServiceBusTopicMessagesConsumerAutoForward;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.ISubscriptionClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;


@Component
public class AutoForwardTargetTopicConsumer {
	


    private ISubscriptionClient iSubscriptionClient1 ;
    private final Logger log = LoggerFactory.getLogger(AutoForwardTargetTopicConsumer.class);
    private String connectionString = "Endpoint=sb://topicsinservicebus.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=ny3fKCHs2eFllaAkmT4VUDw5F+r815o1P2ftwOhZLhI=";
	
	AutoForwardTargetTopicConsumer() {
		try {
			iSubscriptionClient1 = new SubscriptionClient(
					new ConnectionStringBuilder(connectionString, "autoforwardtargettopic/subscriptions/subscription1"),
					ReceiveMode.PEEKLOCK);
		} catch (InterruptedException | ServiceBusException e) {
			e.printStackTrace();
		}
	}
   
    
    
	@EventListener(ApplicationReadyEvent.class)
    public void consume() throws Exception {

    	recievingmessages(iSubscriptionClient1);
    }

    @SuppressWarnings("deprecation")
	public void recievingmessages(ISubscriptionClient iSubscriptionClient) throws InterruptedException, ServiceBusException {


        iSubscriptionClient.registerMessageHandler(new IMessageHandler() {

            @Override
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
                log.info("received message " + new String(message.getBody()) + " with subscription " + iSubscriptionClient.getEntityPath());
                return CompletableFuture.completedFuture(null);
            }
            
            @Override
            public void notifyException(Throwable exception, ExceptionPhase phase) {
                log.error("eeks!", exception);
            }
        });
        
    
    	
    }

}
