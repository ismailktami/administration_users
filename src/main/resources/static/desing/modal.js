// Get the modal
console.log("script modal");
var modal = document.getElementById("myModal");
var modalImg = document.getElementById("img01");
var captionText = document.getElementById("caption");

function clickImage(){
	var img = document.getElementById("myImg");
	console.log(img);
	 modal.style.display = "block";
	  modalImg.src = img.src;
	  captionText.innerHTML = "Image";
	
}
 


// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() { 
  modal.style.display = "none";
}