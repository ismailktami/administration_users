package com.example.demo.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	@Query("select i from Invitation i where i.receiver.id=?1")
	public List<Invitation> getInvitationByReceiver(Long id);

	@Query("select i from Invitation i where i.sender.id=?1")
//	@Query("select i from Invitation i where i.sender.id=:id")@Param("id")

	public List<Invitation> getInvitationBySender(Long id);
	
	

}
