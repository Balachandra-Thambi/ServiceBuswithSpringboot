package com.example.SBSchedulingMessagesToTopic;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.ITopicClient;
import com.microsoft.azure.servicebus.Message;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RestController
public class SchedulingMessagesProducer implements Ordered {

	private final ITopicClient iTopicClient;
	private final Logger log = LoggerFactory.getLogger(SchedulingMessagesProducer.class);

	static final Gson GSON = new Gson();

	SchedulingMessagesProducer(ITopicClient iTopicClient) {
		this.iTopicClient = iTopicClient;
	}

	@PostMapping("/messages")
	public void produce(@RequestParam String message) throws Exception {
		Message message1 = new Message(message);
		message1.setScheduledEnqueuedTimeUtc(Clock.systemUTC().instant().plusSeconds(120));
		this.iTopicClient.send(message1);
	}

	// @SuppressWarnings("serial")
	@PostMapping("/User")
	@SuppressWarnings("serial")
	public void producer(@RequestBody UserDetails user) throws Exception {
		
		final String messageId = Integer.toString(12);
		LocalTime timeaftersend = LocalTime.now();
		
		log.info("Username - " + user.getUserName());
		log.info("Address - " + user.getUAddress());
		log.info("State - " + user.getUstate());

		IMessage message = new Message(GSON.toJson(user, UserDetails.class).getBytes(UTF_8));
		message.setContentType("application/json");
		message.setMessageId(messageId);
		//message.setScheduledEnqueuedTimeUtc(Clock.systemUTC().instant().plusSeconds(120));
		
		  Instant scheduleTime = Clock.systemUTC().instant().plusSeconds(120);
		  iTopicClient.scheduleMessageAsync(message, scheduleTime);
		 

		message.setProperties(new HashMap<String, Object>() {
			{
				put("UserName", user.getUserName());
				put("Address", user.getUAddress());
				put("State", user.getUstate());
			}
			
		});
		
		//this.iTopicClient.send(message);
		log.info("Time after sending messages - " + timeaftersend);

	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
