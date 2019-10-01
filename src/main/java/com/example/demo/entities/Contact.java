package com.example.demo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="contacts")
public class Contact {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@OneToOne(fetch=FetchType.EAGER,cascade={CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="user_id")
	@JsonIgnoreProperties(value= {"password","email"})

	private User user;
	
	@OneToOne(fetch=FetchType.EAGER,cascade={CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="contact_id")
	@JsonIgnoreProperties(value= {"password","email"})
	private User contact;
	
	
	private boolean bloquer;
	private boolean proche;
	private String type_amiter;
	public boolean isBloquer() {
		return bloquer;
	}
	public void setBloquer(boolean bloquer) {
		this.bloquer = bloquer;
	}
	public boolean isProche() {
		return proche;
	}
	public void setProche(boolean proche) {
		this.proche = proche;
	}
	public String getType_amiter() {
		return type_amiter;
	}
	public void setType_amiter(String type_amiter) {
		this.type_amiter = type_amiter;
	}
	
	
	public User getUser() {
		return user;
	}
	public User getContact() {
		return contact;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setContact(User contact) {
		this.contact = contact;
	}
	
}
