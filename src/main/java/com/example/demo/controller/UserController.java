package com.example.demo.controller;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;

import org.springframework.stereotype.Controller;

import com.example.demo.entities.Contact;
import com.example.demo.entities.ContactRepository;
import com.example.demo.entities.Invitation;
import com.example.demo.entities.InvitationRepository;
import com.example.demo.entities.LastMessage;
import com.example.demo.entities.LastMessgaeRepository;
import com.example.demo.entities.Message;
import com.example.demo.entities.MessageRepository;
import com.example.demo.entities.User;
import com.example.demo.entities.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRep;
	
	@Autowired
	private MessageRepository mRep;
	
	@Autowired
	private LastMessgaeRepository lastMessgaeRepository;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	
	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	private InvitationRepository invRep;
	@Autowired
	private ContactRepository contactRepository;
	
	@MessageMapping("/getContacts")
	@SendToUser
	public List<User> getContacts(Principal p){
		this.getContactsbyUser(p);
		String username=p.getName();
		Optional<User> u=userRep.findById(userRep.findByUsername(username).getIdUt());
		return u.get().getContacts();		
	}
	
	@MessageMapping("/getContactsbyUser")
	@SendToUser
	public List<User> getContactsbyUser(Principal p){
		List<Contact> liste= contactRepository.getContact(userRep.findByUsername(p.getName()).getIdUt());
		List<User> listeUser=new ArrayList<>();
		liste.forEach(e->{
			e.getContact().setPassword(null);
			e.getUser().setPassword(null);
			if(e.getUser().getUsername().equals(p.getName()))
				listeUser.add(e.getContact());
			else
				listeUser.add(e.getUser());
		});
		return listeUser;
	}
	
	
	
	
	public User bloquerContact(Principal p,String message) {

		return null;
		
	}
	
	@MessageMapping("/getInvitationRecu")
	@SendToUser
	public List<Invitation> getInvitationRecu(Principal p){
		String username=p.getName();
		Optional<User> u=userRep.findById(userRep.findByUsername(username).getIdUt());
		User user=u.get();
		return invRep.getInvitationByReceiver(user.getIdUt());
	}
	
	
	@MessageMapping("/getInvitationEnvoyer")
	@SendToUser
	public List<Invitation> getInvitationEnvoyer(Principal p){
		String username=p.getName();
		Optional<User> u=userRep.findById(userRep.findByUsername(username).getIdUt());
		User user=u.get();
		return invRep.getInvitationBySender(user.getIdUt());
	}
	
	@MessageMapping("/doInvitation")
	@SendToUser
	public   User DoInvitation(Principal p, @JsonProperty User message)  throws JsonProcessingException {
		Optional<User> u=userRep.findById(userRep.findByUsername(p.getName()).getIdUt());
		Optional<User> u2=userRep.findById(userRep.findByUsername(message.getUsername()).getIdUt());
		User user=u.get();
		User user2=u2.get();
		Invitation i=new Invitation();
		i.setDate_invitation(new Date());
		i.setReceiver(user2);
		i.setSender(user);
		Invitation iii=invRep.save(i);
	
		
		return invRep.save(i).getReceiver();

	
	}
	
	
	@MessageMapping("/addContact")
	@SendToUser
	@org.springframework.transaction.annotation.Transactional
	public   boolean addContact(Principal p, @JsonProperty User message)  throws JsonProcessingException {
		
		entityManager.createNativeQuery("insert into contacts(user_user_id,contacts_user_id,bloquer,proche,type_amiter) values(?,?,?,?,?)")
	      .setParameter(1,userRep.findByUsername(p.getName()).getIdUt())
	      .setParameter(2,message.getIdUt())
	      .setParameter(3, false)
	      .setParameter(4, true)
	      .setParameter(5, "kk")
	      .executeUpdate();
		   return true;
	
	}
	

	@MessageMapping("/getMessages")
	@SendToUser
	@org.springframework.transaction.annotation.Transactional
	public   List<Message> getMessagebyDestination(Principal p, String destination)  throws JsonProcessingException {
	 List<Message> messages=new ArrayList<>();
	 messages=mRep.getMessageByContact(userRep.findByUsername(p.getName()).getIdUt(),userRep.findByUsername(destination).getIdUt());
	
	 return messages;
	}
	
	
	
	@MessageMapping("/getMessages.{page}")
	@SendToUser
	@org.springframework.transaction.annotation.Transactional
	public   void getMessages(Principal p, String destination,@DestinationVariable("page")  int page)  throws JsonProcessingException {
	 simpMessagingTemplate.convertAndSendToUser(p.getName(),"/queue/getMessages",mRep.getMessageByContactByPage(userRep.findByUsername(p.getName()).getIdUt(),userRep.findByUsername(destination).getIdUt(),new PageRequest(page, 30)));

	}
	
	
	
	@MessageMapping("/sendMessage")
	@SendToUser
	@org.springframework.transaction.annotation.Transactional
	public   boolean sendMessage(Principal p,String message) {
		String msg=message.split(";")[0].toString();
		String destination=message.split(";")[1];
		lastMessgaeRepository.deleteLast(userRep.findByUsername(p.getName()).getIdUt(),userRep.findByUsername(destination).getIdUt());
		
		lastMessgaeRepository.save(new LastMessage(msg,new Date(),userRep.findByUsername(p.getName()), userRep.findByUsername(destination)));
		
		try {
		entityManager.createNativeQuery("insert into message(date,message,receiver_user_id,sender_user_id,type) values(?,?,?,?,?)")
	      .setParameter(1,new Date())
	      .setParameter(2,msg)
	      .setParameter(3,userRep.findByUsername(destination).getIdUt())
	      .setParameter(4, userRep.findByUsername(p.getName()).getIdUt())
	      .setParameter(5,"text")
	      .executeUpdate();
		   return true;
		}
		catch(HibernateException h) {
			return false;
		}
		
			}

	
	@MessageMapping("/sendMessageImage.{destination}")
	@org.springframework.transaction.annotation.Transactional

	public boolean sendMessageImage(Principal p,@Payload String message,@DestinationVariable("destination") String destination) throws IOException {
			Decoder decoder = Base64.getDecoder();
	        byte[] decodedByte = decoder.decode(message.split(",")[1]);
	        try {
	    		entityManager.createNativeQuery("insert into message(date,message,receiver_user_id,sender_user_id,image,type) values(?,?,?,?,?,?)")
	    	      .setParameter(1,new Date())
	    	      .setParameter(2,"")
	    	      .setParameter(3,userRep.findByUsername(destination).getIdUt())
	    	      .setParameter(4, userRep.findByUsername(p.getName()).getIdUt())
	    	      .setParameter(5,decodedByte)
	    	      .setParameter(6,"image")
	    	      .executeUpdate();
	    		   simpMessagingTemplate.convertAndSendToUser(destination,"/queue/sendMessageImage",message);
	    		   return true;
	    		}
	    		catch(HibernateException h) {
	    			return false;
	    		}
	         	        
			}
	
	
	@MessageMapping("/getImage.{id}")
	@SendToUser
	public void getImage(Principal p,@DestinationVariable("id") int id) {
		String decoder = Base64.getEncoder().encodeToString(mRep.getOne((long)id).getImage());	
		HashMap<String, String> mapImage=new HashMap<>();
		String image= "data:application/octet-stream;base64,"+decoder;
		mapImage.put("id",""+id);
		mapImage.put("image",image);
		
		simpMessagingTemplate.convertAndSendToUser(p.getName(),"/queue/getImage",mapImage);

	}
	
	
	
	
		
	
}
