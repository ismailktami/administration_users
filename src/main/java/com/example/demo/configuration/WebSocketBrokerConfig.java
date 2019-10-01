package com.example.demo.configuration;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.example.demo.entities.UserRepository;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {
	

	
	private UserRepository urep;
	
	public static String getName() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (!(authentication instanceof AnonymousAuthenticationToken)) {
	    String currentUserName = authentication.getName();
	    return currentUserName;
	}
	
    return null;
}
	
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/questions").setHandshakeHandler(new UserSocket()).withSockJS();
		registry.addEndpoint("/questions").addInterceptors(new HttpHandshakeInterceptor()).withSockJS();
		
		
	}

	
	 @Override
     public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
         registration.setMessageSizeLimit(3000000); // default : 64 * 1024
         registration.setSendTimeLimit(20 * 1000000); // default : 10 * 10000
         registration.setSendBufferSizeLimit(3* 512 * 1024); // default : 512 * 1024

     }
	 
	 
	 
class QuestionHandler extends TextWebSocketHandler {
	
	
	List<WebSocketSession> sessions=new CopyOnWriteArrayList<WebSocketSession>();
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		for(WebSocketSession s:sessions) {
			s.sendMessage(message);
			
		}
		
	}
	public List<WebSocketSession> getSessions() {
		return sessions;
	}
	
}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app")
		.enableSimpleBroker("/topic","/queue");
		
	}
	
	private class UserSocket extends DefaultHandshakeHandler
	{
		@Autowired
		private String username;
			@Override
			protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
					Map<String, Object> attributes) {
				username=getName();
				return new UsernamePasswordAuthenticationToken(getName(),null);


				
			}
					
		
	}
	@Component
	public class HttpHandshakeInterceptor implements HandshakeInterceptor {
		@Autowired
		private SimpMessagingTemplate simpMessagingTemplate;
		@Autowired
		private UserRepository userrep;
		@Override
		public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
				Map attributes) throws Exception {
			if (request instanceof ServletServerHttpRequest) {
				ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
				HttpSession session = servletRequest.getServletRequest().getSession();
				attributes.put("sessionId", session.getId());
				System.err.println(session.getId());
				
				//simpMessagingTemplate.convertAndSend("/topic/addUser", userrep.findByUsername(username));
			
			}
			return true;

		}

		

		@Override
		public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
				Exception exception) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}

	
	
	
}