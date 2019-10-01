package com.example.demo.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
public class LastMessage {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String message;
	@JsonIgnoreProperties({"motDePasse"})
	@ManyToOne(cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	private User sender;
	@JsonIgnoreProperties({"motDePasse"})
	@ManyToOne(cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	private User receiver;
	
	
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public LastMessage(String message,Date d, User sender, User receiver) {
		super();
		this.date=d;
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
	}
	public LastMessage() {
		
	}
	
	
	
}
