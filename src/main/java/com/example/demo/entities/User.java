package com.example.demo.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.type.TrueFalseType;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="users")
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long user_id ;
	private String nom;
	
	private String prenom;
	
	@Column(unique=true)
	private String email;
	
	@Column(unique= true)
	@NonNull
	private String username;
	
	@JsonIgnore
	private String password;
	
	private String photo;
	
	
	private boolean enabled;
	@OneToMany(mappedBy = "user",fetch=FetchType.EAGER,cascade=CascadeType.REMOVE)
	@JsonIgnoreProperties("user")
	@JsonIgnore
	private List<Role> roles;
	
	
	@OneToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch=FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name="contacts",joinColumns= {@JoinColumn(name="user_id")},inverseJoinColumns= {@JoinColumn(name="contact_id")})
	@JsonIgnore
	private List<User> contacts;
	
	
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	
	public List<User> getContacts() {
		return contacts;
	}
	public void setContacts(List<User> contacts) {
		this.contacts = contacts;
	}
	
	
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}


	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getLastMessage() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getMail() {
		return password;
	}

	public void setMail(String mail) {
		this.password = mail;
	}

	@JsonIgnore
	public String getMotDePasse() {
		return password;
	}

	public void setMotDePasse(String motDePasse) {
		this.password = motDePasse;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isActived() {
		return enabled;
	}

	public void setActived(boolean actived) {
		this.enabled = actived;
	}

	public long getIdUt() {
		return user_id;
	}

	public void setIdUt(long idUt) {
		this.user_id = idUt;
	}

	public User(String username, String prenom, String nom) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.username = username;
	}

public void setPassword(String password) {
	this.password = password;
}
public String getPassword() {
	return password;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
	

}
