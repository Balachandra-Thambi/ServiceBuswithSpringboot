package com.example.SBTopicFilterProducerApplication;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.ITopicClient;
import com.microsoft.azure.servicebus.Message;

import lombok.extern.log4j.Log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.time.Clock;
import java.util.HashMap;


@Log4j2
@Component
@Controller
class ServiceBusProducer {

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
    public ModelAndView Register(@ModelAttribute("order") Order order, Model model) {
    	ModelAndView mv = new ModelAndView("Register");
		
		  if (order==null) { 
			  model.addAttribute("Order",new Order());
		  }
    	return mv;
    }
    
    @SuppressWarnings("serial")
	@PostMapping("/TopicFilter")
    public String producer(@ModelAttribute("Order") Order order) throws Exception {
    	final String messageId = Integer.toString(12);
    	
    	log.info("Name - " + order.getName());
    	log.info("Color - " + order.getColor());
		log.info("Quantity - " + order.getQuantity());
		log.info("Priority - " + order.getPriority());
         
    	IMessage message = new Message(GSON.toJson(order, Order.class).getBytes(UTF_8));
        message.setContentType("application/json");
        message.setMessageId(messageId);
        message.setCorrelationId(order.getPriority());
        message.setLabel(order.getColor());
        message.setProperties(new HashMap<String, Object>() {
        	{
        	put("Prodcut Name " , order.getName());
            put("Product Color " , order.getColor());
            put("Product Quantity " , Integer.toString(order.getQuantity()));
            put("Product Priority " , order.getPriority());
            
        	}
		});
        
        iTopicClient.send(message);
        return "Success";
    }
}
