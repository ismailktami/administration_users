package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.annotation.WebFilter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class QuestionController {

	private List<SseEmitter> emitters=new CopyOnWriteArrayList<>();

	
	/*public SseEmitter questions() {
		SseEmitter sse=new SseEmitter();
		emitters.add(sse);
		sse.onCompletion(()->{
			emitters.remove(sse);
		});
		return sse;
	
	
	
	}
	
	*/

	
	
	
	@RequestMapping(value="/new-question",method=RequestMethod.POST)
	public ResponseEntity<String> postQuestion( String question)  {
		for(SseEmitter e: emitters) {
			try {
				e.send(e.event().name("spring").data(question));
			} catch (IOException e1) {

				e1.printStackTrace();		
				return new ResponseEntity<>("error",HttpStatus.INTERNAL_SERVER_ERROR);

			}
		}
		
		return new ResponseEntity<String>("votre message est envoyer",HttpStatus.OK);

		
	}
	
	
	
	
	
	
}
