package com.example.demo.controller;

import com.example.demo.controller.*;
import com.example.demo.entities.User;
import com.example.demo.entities.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class WebSocketEventListener {
	
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
   
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    	
    	System.err.println(event.getUser().getName());

    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
       
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            messagingTemplate.convertAndSend("/topic/diconnect",username);
        }        
    
        
    }
    
    
    
    
  
    
}