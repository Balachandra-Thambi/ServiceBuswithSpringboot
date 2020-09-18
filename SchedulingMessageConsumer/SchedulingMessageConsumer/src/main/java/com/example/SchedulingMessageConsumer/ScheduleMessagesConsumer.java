package com.example.SchedulingMessageConsumer;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.ISubscriptionClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Component
public class ScheduleMessagesConsumer implements Ordered {
	
    private ISubscriptionClient iSubscriptionClient ;
    private ISubscriptionClient iSubscriptionClient1 ;
    private final Logger log = LoggerFactory.getLogger(ScheduleMessagesConsumer.class);
    private String connectionString = "Endpoint=sb://topicsinservicebus.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=ny3fKCHs2eFllaAkmT4VUDw5F+r815o1P2ftwOhZLhI=";
    
    
	@EventListener(ApplicationReadyEvent.class)
    public void consume() throws Exception {
    	iSubscriptionClient = new SubscriptionClient(new ConnectionStringBuilder(connectionString,"schedulingmessages/subscriptions/Subscription"), ReceiveMode.PEEKLOCK);
        iSubscriptionClient1 = new SubscriptionClient(new ConnectionStringBuilder(connectionString,"schedulingmessages/subscriptions/Subscription1"), ReceiveMode.PEEKLOCK);

    	recievingmessages(iSubscriptionClient);
    	recievingmessages(iSubscriptionClient1);
    	
    }

    @SuppressWarnings("deprecation")
	public void recievingmessages(ISubscriptionClient iSubscriptionClient) throws InterruptedException, ServiceBusException {

        iSubscriptionClient.registerMessageHandler(new IMessageHandler() {

            @Override
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
            	LocalTime timeafterrecieve = LocalTime.now();
                log.info("received message " + new String(message.getBody()) + " with body ID " + message.getMessageId() + " at time " + timeafterrecieve);
                log.info("Time after recieving messages - " + timeafterrecieve);
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
