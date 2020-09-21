package com.example.SchedulingMessageProducer;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.ITopicClient;
import com.microsoft.azure.servicebus.Message;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@Controller
public class SchedulingMessagesProducer {

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
	
	 @GetMapping("/")
    public ModelAndView Register(@ModelAttribute("userDetails") UserDetails user, Model model) {
    	ModelAndView mv = new ModelAndView("Register");
		
		  if (user==null) { 
			  model.addAttribute("userDetails",new UserDetails());
		  }
    	return mv;
    }

	// @SuppressWarnings("serial")
	@PostMapping("/SchedulingMessages")
	@SuppressWarnings("serial")
	public String producer(@ModelAttribute("userDetails") UserDetails user) throws Exception {
		
		LocalTime timeaftersend = LocalTime.now();
		
		log.info("Username - " + user.getUserName());
		log.info("Address - " + user.getAddress());
		log.info("State - " + user.getState());

		IMessage message = new Message(GSON.toJson(user, UserDetails.class).getBytes(UTF_8));
		message.setContentType("application/json");
		//message.setScheduledEnqueuedTimeUtc(Clock.systemUTC().instant().plusSeconds(120));
		
		  Instant scheduleTime = Clock.systemUTC().instant().plusSeconds(120);
		  iTopicClient.scheduleMessageAsync(message, scheduleTime);
		 

		message.setProperties(new HashMap<String, Object>() {
			{
				put("UserName", user.getUserName());
				put("Address", user.getAddress());
				put("State", user.getState());
			}
			
		});
		
		//this.iTopicClient.send(message);
		log.info("Time after sending messages - " + timeaftersend);
		return "Success";

	}

}
