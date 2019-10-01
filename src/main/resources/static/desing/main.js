var destination;
var username;

   var messageArea = document.querySelector('#messageArea');
    var page=0;
    var lengthPage;
	var ws;
    var stompClient;    
    ws = new SockJS('/questions');
    stompClient =Stomp.over(ws);
     
    
   stompClient.connect({},function(frame){
	   
	   stompClient.subscribe("/topic/questions",function(message){
		   newMessage();
	   });
	
	   
	   stompClient.subscribe("/user/queue/sendMessageImage",function(message){
	 	   newImage(message.body,"receive");
	 	 
			
		   
	   });
	   
		stompClient.subscribe("/user/queue/getImage",function(message){
			var p=$("p[idimage='"+JSON.parse(message.body).id+"']");
			 p.empty();
			p.prepend('<img class="message"  id="myImg" src="'+JSON.parse(message.body).image+'"  onclick="clickImage()" style="width:200px;height:200px;"/>');
			
			
			  
		});

	   
	   
	   stompClient.subscribe("/user/queue/doInvitation",function(user){
		   
		   stompClient.send("/user/"+JSON.parse(user.body).username+"/queue/invitation",{},username);
		   
		   	
	   });
	   
	   stompClient.subscribe("/user/queue/invitation",function(data){
		   if(confirm("new invitation from "+ data)){
			 
		   }
		   
	   });

	   
	
	   stompClient.subscribe("/user/queue/addContact",function(message){
		   //Subscripbe Event Invitation Envoyer
	   });
	   
	   
	   
	   
	   stompClient.subscribe("/topic/images",function(message){
		   newMessage();

	   });
	   
	   stompClient.subscribe("/topic/diconnect",function(message){
		   var contacts = $("#contacts li");
		   for (var i = 0; i < contacts.length; i++) {
			   var text = $(contacts[i]).find('p.name').text();
		       if ( text == message.body ) { 
		    	   $(contacts[i]).find('p').attr("statu","offline");
		    	   $(contacts[i]).find('span').attr('class','contact-status busy');
		       }
		   } 
	   });
	   stompClient.subscribe("/topic/addUser",function(user){
		   newUserJoin(JSON.parse(user.body));
		 });
	  
	   stompClient.subscribe("/topic/ReplyNotify",function(user){
		   alert(JSON.parse(user.body));
		 });
	   
	   
	   
	  
	   stompClient.subscribe("/user/queue/getMessages",function(messages){
		   
		   console.log(messages);
		   
		   const messagesUl = document.querySelector('#messageList');

		   if(page==0)
		        messagesUl.innerHTML = "";
		
		   var messagesInformations=JSON.parse(messages.body).content;
		   
		   messagesInformations.forEach(function(el){

			 if(el.sender.username === username ){
				 if(el.type==="text")
				 SetSendedMessage(el.message);
				 else
				 setImage(el.type,el.id,"send")
				 
			 }
			 else{
				 if(el.type==="text")
				 SetReceivedMessage(el.message,el.sender.username);
				 else
					 setImage(el.type,el.id,"receive")

			 }
			 });
		   
		   if(page==0)
				scroll();
		   page++;
		   
		
		   
	   });
	   

	   
	   stompClient.subscribe("/user/queue/getContactsbyUser",function(listContacts){
		   var contacts=  JSON.parse(listContacts.body);
		   console.log(contacts);
	   });
	   
	   
	    
	   stompClient.subscribe("/user/queue/getUserInfo",function(user){
		 		   username=JSON.parse(user.body).username;
		   $("#usernameProfile").append(username+'<i class="fa fa-comments" id="sharedesktop"  style="margin-left:30px;cursor:pointer;"   aria-hidden="true"></i><i class="fa fa-users" style="margin-left:40px;cursor:pointer"  onclick="showinvitations()"></i>');
		 });
	   
	   
	   stompClient.subscribe("/user/queue/private",function(data){
		   var message=JSON.parse(data.body);
		   if(message.type=='message'){
			   newMessageReceived(message);			   
		   }
		   
		   else{
			   var contacts = $("#contacts li");
			   for (var i = 0; i < contacts.length; i++) { 
			   	   
			   	   var text = $(contacts[i]).find('p.name').text();
			       if ( text == message.username ) { 
			    	   $(contacts[i]).find('span').attr('class','contact-status online');
			       }
			   }
			   $("#contacts").animate({ scrollTop: $(document).height() }, "fast");
			   
		   }
		   
		   
		   
		});
	   stompClient.subscribe("/user/queue/noificationuser",function(data){		   
		   user=JSON.parse(data.body).username;
		   var i=0;
		   var users = null;
		   var contacts=$("#contacts li");
		   $("div .meta p").each(function(index){
			   if($(this).attr('id')=="namefilter"){
				   if ($(this).text()==user) { 
					   $(this).attr('statu','enligne')
			    	   $(contacts[i]).find('span').attr('class','contact-status online');
			       }
				   i++;
			   }

		   });
		
	   });
	   getUserInfo();
	   //getContacts();
	   getAllUsers();
	   
	   stompClient.subscribe("/user/queue/getAllUsers",function(listusers){
		 var users=  JSON.parse(listusers.body);
		 for(var i=0;i<users.length;i++){
		$('<li  class="contact item-user" onclick="changeDestination(this)"><div class="wrap"> <span class="contact-status busy"></span><img src="http://emilcarlsson.se/assets/louislitt.png" alt="" /><p><div class="meta"><p class="name" id="namefilter" statu="offline">'+users[i].username+'</p><p class="preview">'+users[i].lastMessage+'</p></div></div></li>').appendTo($('#contacts ul'));
		 }
		   AddUserNotify();
	   	});	 
	   
	   
	   stompClient.subscribe("/user/queue/getInvitationRecu",function(listInvitations){
			 var invitations=  JSON.parse(listInvitations.body);
			/* for(var i=0;i<users.length;i++){
							 $('<li class="contact" onclick="changeDestination(this)"><div class="wrap"> <span class="contact-status busy"></span><img src="http://emilcarlsson.se/assets/louislitt.png" alt="" /><p><div class="meta"><p class="name" id="namefilter">'+users[i].username+'</p><p class="preview">You just got LITT up, Mike.</p></div></div></li>').appendTo($('#contacts ul'));

			 }
			 */
			 
			 console.log(invitations.body);
			 
			 
		   	});	   
	   
	   stompClient.subscribe("/user/queue/getInvitationEnvoyer",function(listInvitations){
			 var invitations=  JSON.parse(listInvitations.body);
			/* for(var i=0;i<users.length;i++){
							 $('<li class="contact" onclick="changeDestination(this)"><div class="wrap"> <span class="contact-status busy"></span><img src="http://emilcarlsson.se/assets/louislitt.png" alt="" /><p><div class="meta"><p class="name" id="namefilter">'+users[i].username+'</p><p class="preview">You just got LITT up, Mike.</p></div></div></li>').appendTo($('#contacts ul'));

			 }
			 */
			 
			 console.log(invitations.body);
			 
			 
		   	});	   
	   
	   
	   stompClient.subscribe("/user/queue/getMessagesbyPage",function(message){
		  console.log(message); 
	   });
	   
	   

	   stompClient.subscribe("/user/queue/getAllUsersOnLine2",function(listusers){
		 var a=  JSON.parse(listusers.body);
		 console.log(a[0].nom);
	   	});	   
   },function(error){
	   console.log("STMP protocol error "+error);
	   
   });
   
  
   

 function sendMessage(){
	
	 stompClient.send("/app/questions",{},$(".message-input input").val());
		
	};
	
	
	
	
function sendPrivateMessage(user){
	 var message={type:'message',msg:$(".message-input input").val(),
		 sender:username};
	 
	 stompClient.send("/app/sendMessage",{},$(".message-input input").val()+";"+destination);	

	 stompClient.send("/user/"+destination+"/queue/private",{},JSON.stringify(message));	
}	

function changeDestination(li){	
	$("#contentmessages").show();	
	 //video mode pipe
	// $("#peerVideo").
	
	 page=0;
	 var currentdestination=$(li).find('p.name').text();
	
	if(destination !== currentdestination){
		destination=currentdestination;
	$('.contact-profile').find('p').text(destination);
	$(li).find('p.preview i').attr('class','fa fa-eye').remove();
	//charger messages
	stompClient.send("/app/getMessages."+page,{},destination);
	
	}

}


function sendInvitation(user){
	stompClient.send("/app/doInvitation",{},user);
}
	
function AddUserNotify(){
	 stompClient.send("/app/addUser",{},"joined");
}

function ReplyNotify(userNotify){
	 var message={username:username};
	 stompClient.send("/user/"+userNotify+"/queue/noificationuser",{},JSON.stringify(message));	
}

function getAllUsers(){
	 stompClient.send("/app/getAllUsers");
	
}
function getContacts(){
	 stompClient.send("/app/getContactsbyUser");

}



function getUserInfo(){
	
	 stompClient.send("/app/getUserInfo");

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
    
    
  



//$(".messages").animate({ scrollTop: $(document).height() }, "fast");

$("#profile-img").click(function() {
	$("#status-options").toggleClass("active");
});

$(".expand-button").click(function() {
  $("#profile").toggleClass("expanded");
	$("#contacts").toggleClass("expanded");
});



$("#status-options ul li").click(function() {
	$("#profile-img").removeClass();
	$("#status-online").removeClass("active");
	$("#status-away").removeClass("active");
	$("#status-busy").removeClass("active");
	$("#status-offline").removeClass("active");
	$(this).addClass("active");
	
	if($("#status-online").hasClass("active")) {
		$("#profile-img").addClass("online");
	} else if ($("#status-away").hasClass("active")) {
		$("#profile-img").addClass("away");
	} else if ($("#status-busy").hasClass("active")) {
		$("#profile-img").addClass("busy");
	} else if ($("#status-offline").hasClass("active")) {
		$("#profile-img").addClass("offline");
	} else {
		$("#profile-img").removeClass();
	};
	
	$("#status-options").removeClass("active");
});

function newMessage() {
	message = $(".message-input input").val();
	if($.trim(message) == '') {
		return false;
	}
	$('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + message + '</p></li>').appendTo($('.messages ul'));
	$('.message-input input').val(null);
	$('.contact.active .preview').html('<span>You: </span>' + message);

	scroll();
	

};



function newImage(file,type) {
	if(type==="send"){
	$('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p class="message "><img class="message " id="myImg" alt="Snow" src="'+file+'" onclick="clickImage()" style="width:200px;height:200px;"/><u>'+file.name+'.png</u><i class="fa fa-download fa-fw" style="color:green;></i></p></li>').appendTo($('.messages ul'));

	}
	else{
		$('<li class="replies"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p class="message"><img class="message"  id="myImg" src="'+file+'"  onclick="clickImage()" style="width:200px;height:200px;"/><u>'+file.name+'.png</u><i class="fa fa-download fa-fw" style="color:green;></i></p></li>').appendTo($('.messages ul'));
	}
	
	

};



function setImage(filename,id,type){
	if(type==="send"){
		$('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p class="message" id="imageurl" idimage='+id+' style="cursor:pointer;" onclick="getImage(this)" ><u>'+filename+'.png</u><i class="fa fa-download fa-fw" style="color:green;"></i></p></li>').prependTo($('.messages ul'));
		}
		else{
			$('<li class="replies"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p class="message" id="imageurl" idimage='+id+' style="cursor:pointer;" onclick="getImage(this)" ><u style="color:black">Image.png</u><i class="fa fa-download fa-fw" style="color:green;"></i></p></li>').prependTo($('.messages ul'));
		}
		
		
	
	
}


function SetSendedMessage(message) {
	
	$('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + message + '</p></li>').prependTo($('.messages ul'));
	$('.message-input input').val(null);
	$('.contact.active .preview').html('<span>You: </span>' + message);
};


function getImage(pimage){
		var p=pimage;	
		var id=$(p).attr("idimage");
		stompClient.send("/app/getImage."+id,{},{});
			

}


function SetReceivedMessage(message,sender){
	if(sender==destination){

	if($.trim(message) == '') {
		return false;
	}
	$('<li class="replies"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + message + '</p></li>').prependTo($('.messages ul'));
		$('.contact.active .preview').html('<span>You: </span>' + message);
	
	}
	else{
		var li;
		var contacts = $("#contacts li");
		   for (var i = 0; i < contacts.length; i++) { 
			   var text = $(contacts[i]).find('p.name').text();
		       if ( text == sender ) { 
		    	   li= $(contacts[i]);
		    	   break;
		    	 
		       	}
		   }
		   $(li).find('p.preview').html(message+'<i class="fa fa-phone-slash fa-lg"  aria-hidden="true" style="color:red; float:right;"></i>');
	}

	}




function newMessageReceived(data) {
	var message=data.msg;
	var sender=data.sender;
	
	
	if(sender==destination){
	if($.trim(message) == '') {
		return false;
	}
	$('<li class="replies"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + message + '</p></li>').appendTo($('.messages ul'));
	scroll();
	$('.contact.active .preview').html('<span>You: </span>' + message);
	
	}
	else{
		var li;
		var contacts = $("#contacts li");
		   for (var i = 0; i < contacts.length; i++) { 
			   var text = $(contacts[i]).find('p.name').text();
		       if ( text == sender ) { 
		    	   li= $(contacts[i]);
		    	   break;
		    	 
		       	}
		   }
		   $(li).find('p.preview').html(message+'<i class="fa fa-eye-slash fa-lg"  aria-hidden="true" style="color:red; float:right;"></i>');
		  

		

		   
	}
	

	
	};


function newUserJoin(message){
	if(message.username==username)
		return;
	
var users = $("div .meta p");
var contacts=$("#contacts li");
var i=0;
$(users).each(function(index){
   if($(this).attr('id')=="namefilter"){
	   if ( $(this).text() === message.username){
		   $(this).attr("statu","enligne");
		   $(contacts[i]).find('span').attr('class','contact-status online');
	     }
  	   i++;

   }
});



$("#contacts").animate({ scrollTop: $(document).height() }, "fast");

ReplyNotify(message.username);


}





$('.submit').click(function() {
	sendPrivateMessage(destination);
});

$(window).on('keydown', function(e) {
  if (e.which == 13) {
    sendPrivateMessage(destination);
	newMessage(); 
return false;
  }
});


const bntfile=document.getElementById("btnfile");
const inputfile=document.getElementById("inputfile");




btnfile.addEventListener("click",function(){
		inputfile.click();
});

inputfile.addEventListener('change', handleFileSelect, false);


function handleFileSelect(evt) {
    var files = evt.target.files; // FileList object
    // Loop through the FileList and render image files as thumbnails.
    for (var i = 0, f; f = files[i]; i++) {
       var fileAsBlob = new Blob([f]);
      	var reader = new FileReader();
 	    reader.readAsDataURL(fileAsBlob); 
      
	   var base64data;

 	   reader.onload = function(e) {
    	   base64data = reader.result;                
           stompClient.send("/app/sendMessageImage."+destination,{},base64data);
           var message =
           {'message':base64data,'destination':destination,type:'image'};
            newImage(base64data,"send");

        	 // stompClient.send("/user/"+destination+"/queue/sendMessageImage",{},JSON.stringify(message));	
			
      	};

   
    
    }
    files=[];
    
    
  }
function scroll(){
	$.fn.scrollDown=function(){
	    let el=$(this)
	    el.scrollTop(el[0].scrollHeight)
	}

	$('div.messages').scrollDown()

}







$( document ).ready(function() {
$("#imageurl").click(function(){
	console.log(this);
})

$('div.messages').scroll(function(){
	if($('div.messages').scrollTop()==0)
		{
		stompClient.send("/app/getMessages."+page,{},destination);
			}	

	
});



});

function showinvitations(){
	$("#contentinvitations").show();
	$("#contentmessages").hide();
}

function showmessages(){
	$("#contentinvitations").hide();
	$("#contentmessages").show();
}




