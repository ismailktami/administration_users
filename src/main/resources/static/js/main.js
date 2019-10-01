    // var source=new EventSource('/questions');
    /* source.addEventListener("spring",function(event){
     
 	   
 	   
 	console.log(event.data);	
     var messageElement = document.createElement('li');
     var textElement = document.createElement('p');
     var messageText = document.createTextNode(event.data);
     textElement.appendChild(messageText);
     messageElement.appendChild(textElement);
     messageArea.appendChild(messageElement);
     });
    */
    /*   function sendForm(){
	$.post('/new-question',$('#newQuestionForm').serialize());
	
}

*/
    var ws;
    var stompClient;
    var messageArea = document.querySelector('#messageArea');
 	
       ws = new SockJS('/questions');
       stompClient =Stomp.over(ws);
     


   	 /*
       ws.onmessage = function(data){
	   console.log(data);	
	    var messageElement = document.createElement('li');
	    var textElement = document.createElement('p');
	    var messageText = document.createTextNode(data);
	    textElement.appendChild(messageText);
	    messageElement.appendChild(textElement);
	    messageArea.appendChild(messageElement);
	   
   };
   
   */
   
   stompClient.connect({},function(frame){


	   
	   stompClient.subscribe("/topic/questions",function(message){
		  	var messageElement = document.createElement('li');
		    var textElement = document.createElement('p');
		    var messageText = document.createTextNode(message.body);
		    textElement.appendChild(messageText);
		    messageElement.appendChild(textElement);
		    messageArea.appendChild(messageElement);

	   });
	   stompClient.subscribe("/topic/diconnect",function(message){

		   console.log("disconnect");
		   
	   });
	   stompClient.subscribe("/topic/addUser",function(message){
			var messageElement = document.createElement('li');
		    var textElement = document.createElement('p');
		    var messageText = document.createTextNode(message.body+" Joined ");
		    textElement.appendChild(messageText);
		    messageElement.appendChild(textElement);
		    messageArea.appendChild(messageElement);		
	   });
	   
	   
	   stompClient.subscribe("/user/queue/private",function(message){
			var messageElement = document.createElement('li');
		    var textElement = document.createElement('p');
		    var messageText = document.createTextNode(message.body);
		    textElement.appendChild(messageText);
		    messageElement.appendChild(textElement);
		    messageArea.appendChild(messageElement);
	   	});
	   
	 
	   AddUserNotify();
	   
	   

	   stompClient.subscribe("/user/queue/getAllUsersOnLine",function(listusers){
		 var a=  JSON.parse(listusers.body);
		 console.log(a[0].nom);
	   	});	   
	   
	   

	   stompClient.subscribe("/user/queue/getAllUsersOnLine2",function(listusers){
		 var a=  JSON.parse(listusers.body);
		 console.log(a[0].nom);
	   	});	   
   },function(error){
	   console.log("STMP protocol error "+error);
	   
   });
   
  
   
   

 function sendForm(){
	 stompClient.send("/app/questions",{},$("#questiontext").val());
		
	};
function AddUserNotify(){
	
	 stompClient.send("/app/addUser",{},"joined");
		
}

function getAllUsersOnLine(){
	
	 stompClient.send("/app/getAllUsersOnLine");
	
}

function getAllUsersOnLine2(){
	
	 stompClient.send("/app/getAllUsersOnLine2");
	
}
 
    
    $(document).ready(function(){	
    
    	$("#users").click(function(){
    		getAllUsersOnLine();
    	});
    	$("#users2").click(function(){
    		getAllUsersOnLine2();
    	});
    	
    	
        });
    
    
    $(window).on('keydown', function(e) {
    	  if (e.which == 13) {
    		  sendForm();
      		$("#questiontext").val("");
      		return false;
    	  }
    	});
    
  
