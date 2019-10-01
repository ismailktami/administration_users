package com.example.demo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Invitation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private Date date_invitation;
	
	
	@ManyToOne
	private User receiver;
	

	@ManyToOne
	private User sender;


	
	private boolean accepter;
	
	
	
	public boolean isAccepter() {
		return accepter;
	}


	public void setAccepter(boolean accepter) {
		this.accepter = accepter;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getDate_invitation() {
		return date_invitation;
	}


	public void setDate_invitation(Date date_invitation) {
		this.date_invitation = date_invitation;
	}


	public User getReceiver() {
		return receiver;
	}


	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}


	public User getSender() {
		return sender;
	}


	public void setSender(User sender) {
		this.sender = sender;
	}
	
	
	
	
	
	
	
}
