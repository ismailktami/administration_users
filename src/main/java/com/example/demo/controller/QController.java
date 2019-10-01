package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.WebSocketSession;

import com.example.demo.entities.LastMessage;
import com.example.demo.entities.LastMessgaeRepository;
import com.example.demo.entities.User;
import com.example.demo.entities.UserRepository;


@Controller
public class QController {

	@Autowired
	private UserRepository urep;
	@Autowired
	private LastMessgaeRepository lastMessage;
	
	 @Autowired
	 private SimpMessageSendingOperations messagingTemplate;
	 
		private List<SseEmitter> emitters=new CopyOnWriteArrayList<>();

	@MessageMapping("/questions")
	public String processQuestion(String question,Principal p) {
		return question.toUpperCase()+" by" +p.getName();
	}

	
    @MessageMapping("/addUser")
    @SendTo("/topic/addUser")
    public User addUser(String message,Principal p, SimpMessageHeaderAccessor headerAccessor) {
    	String username=p.getName();
    	headerAccessor.getSessionAttributes().put("username",username);
    	User u0r=urep.findByUsername(username);
    	User u=new User(username,u0r.getLastMessage(),u0r.getLastMessage());
    	return u;    
    	}
    
    
    
	
    @MessageMapping("/ReplyNotify")
    @SendToUser("/user/asmae/queue/private")
    public User ReplyNotify(Principal p, SimpMessageHeaderAccessor headerAccessor) {
    	String username=p.getName();
    	User u0r=urep.findByUsername(username);
    	User u=new User(username," "," ");
    	return u;    }

    @MessageMapping("/getUserInfo")
    @SendToUser
    public User getUserInfo(Principal  p, SimpMessageHeaderAccessor headerAccessor) {
    	String username=p.getName();
    	headerAccessor.getSessionAttributes().put("username",username);
    	User u0r=urep.findByUsername(username);
    	return u0r;   
    	}
    
    
    
    @MessageMapping("/getAllUsers")
    @SendToUser
    public List<User> getAllUsersOnLine(Principal p){
    	List<User> users =new ArrayList<>();
    	List<User> liste=new ArrayList<User>();
    	users=urep.findAll();
    	users.forEach(e->{
    		if(!e.getUsername().equals(p.getName())) {
    		LastMessage  msg=lastMessage.getLastMessage(urep.findByUsername(p.getName()).getIdUt(), urep.findByUsername(e.getUsername()).getIdUt());
    			if(msg!=null) {
    			liste.add(new User(e.getUsername(),e.getPrenom(),msg.getMessage()));
    			}
    			else {
        			liste.add(new User(e.getUsername(),e.getPrenom(),"Start the discussion"));

    			}
    			}
    		
    			});
    
    	return liste;
    }
     
    
    @MessageMapping("/getAllUsersOnLine2")
    @SendToUser
    public List<User> getAllUsersOnLine2(){
    	List<User> users =new ArrayList<>();
    	
    	users.add(new User("majdoub","asmae","ktamiismail"));
    	users.add(new User("majdoub","asmae","ktamiismail"));
    	users.add(new User("majdoub","asmae","ktamiismail"));
    	users.add(new User("majdoub","asmae","ktamiismail"));
    	
    	
    	
    	return users;
    }
    
 
    
}
