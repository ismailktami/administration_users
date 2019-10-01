package com.example.demo.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContactRepository extends JpaRepository<Contact, Long>{

	
	@Query("select c from Contact c  where c.user.user_id=?1  or c.contact.user_id=?1 ")
	public List<Contact> getContact(long id_user);
	
	

	@Query("select c from Contact c  where c.user.user_id=?1 and c.bloquer=true")
	public List<Contact> getContactsListRouge(long id_user);
	
	
	
	
	
}
