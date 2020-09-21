package com.example.ServiceBusTopicMessagesProducer;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.ITopicClient;
import com.microsoft.azure.servicebus.Message;

import lombok.extern.log4j.Log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Clock;
import java.util.HashMap;


@Log4j2
@Component
@Controller
class ServiceBusProducer implements Ordered {

    private final ITopicClient iTopicClient;
    private final Logger log = LoggerFactory.getLogger(ServiceBusProducer.class);
    static final Gson GSON = new Gson();
	
    ServiceBusProducer(ITopicClient iTopicClient) {
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
    public ModelAndView Register(@ModelAttribute("user") User user, Model model) {
    	ModelAndView mv = new ModelAndView("Register");
		
		  if (user==null) { 
			  model.addAttribute("user",new User());
		  }
    	return mv;
    	
    }
    
    @SuppressWarnings("serial")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String producer(@ModelAttribute("user") User user , Model model) throws Exception {
    
    	final String messageId = Integer.toString(12);
    	
    	log.info("Username - " + user.getUserName());
		log.info("Address - " + user.getAddress());
		log.info("State - " + user.getState());
         
    	IMessage message = new Message(GSON.toJson(user, User.class).getBytes(UTF_8));
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
 
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
