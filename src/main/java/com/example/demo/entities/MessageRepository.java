package com.example.demo.entities;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("select m from Message m where ( m.sender.user_id=?1 and m.receiver.user_id=?2) or ( m.sender.user_id=?2 and m.receiver.user_id=?1) order by m.date asc   ")
	public List<Message> getMessageByContact(long id_sender, long id_receiver);
	
	
	@Query("select m from Message m where ( m.sender.user_id=?1 and m.receiver.user_id=?2) or ( m.sender.user_id=?2 and m.receiver.user_id=?1) order by m.date desc   ")
	public Page<Message> getMessageByContactByPage(long id_sender, long id_receiver, PageRequest page);
	

}
