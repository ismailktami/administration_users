package com.example.demo.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LastMessgaeRepository extends JpaRepository<LastMessage,Long>{
	@Query("select m from LastMessage m where (sender.user_id=:sender and receiver.user_id=:receiver) or (sender.user_id=:receiver and receiver.user_id=:sender)")
	public LastMessage getLastMessage(@Param("sender") long sender, @Param("receiver") long receiver);
	@Transactional
	@Modifying
	@Query("delete from LastMessage where (sender.user_id=:sender and receiver.user_id=:receiver) or (sender.user_id=:receiver and receiver.user_id=:sender)")
	public int deleteLast(@Param("sender") long sender, @Param("receiver") long receiver);
}
