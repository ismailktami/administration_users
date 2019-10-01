   
	console.log("search script");
	let searchBox = document.querySelector('#searchcontact');
    searchBox.addEventListener('input',(e)=>{
  
    	console.log("hey search");
    	let messages = Array.from(document.getElementsByClassName('name'));
    	console.log(messages);

    	let messagesList=Array.from(document.getElementsByClassName('item-user'));
        let i=0;
    
        
    	console.log(messagesList);

        messages.forEach((msg)=>{
            if(msg.innerText.toLowerCase().indexOf(e.target.value.toLowerCase()) === -1){
                messagesList[i].style.display="none";

            }else{
                messagesList[i].style.display="block";
            }
            i++;

        })
    })