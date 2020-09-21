package com.example.ServiceBusTopicMessagesProducerAutoForward;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Clock;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.ITopicClient;
import com.microsoft.azure.servicebus.Message;

@Component
@Controller
public class AutoForwardSourceTopicProducer {
	


    private final ITopicClient iTopicClient;
    private final Logger log = LoggerFactory.getLogger(AutoForwardSourceTopicProducer.class);
    static final Gson GSON = new Gson();
	
    AutoForwardSourceTopicProducer(ITopicClient iTopicClient) {
		this.iTopicClient = iTopicClient;
	}
    
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("serial")
	@PostMapping("/autoforward")
    public String producer(@ModelAttribute("userDetails") UserDetails user) throws Exception {
    	final String messageId = Integer.toString(12);
    	
    	log.info("Username - " + user.getUserName());
		log.info("Address - " + user.getAddress());
		log.info("State - " + user.getState());
		log.info("subscription - " + iTopicClient.getEntityPath());
         
    	IMessage message = new Message(GSON.toJson(user, UserDetails.class).getBytes(UTF_8));
        message.setContentType("application/json");
        message.setMessageId(messageId);
        message.setProperties(new HashMap<String, Object>() {{
            put("UserName", user.getUserName());
            put("Address", user.getAddress());
            put("State", user.getState());
            
        }});
        iTopicClient.send(message);
        return "Success";
    }
 
}
