
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {

	if (config.socketUrl) {
		let subs = config.admin ? 

			["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
		ws.initialize(config.socketUrl, subs);
	}

	var pageURL = window.location.href;
	var lastURLSegment = pageURL.substr(pageURL.lastIndexOf('/') + 1);
	console.log(lastURLSegment);
	go(config.rootUrl + "event/m/"+lastURLSegment,"POST",null).then(e => listUsers(e,"updateMessages"));
	go(config.rootUrl + "event/u/"+lastURLSegment,"POST",null).then(e => listUsers(e,"updateUsersEvent"));

	//response.forEach(e => );

	// add your after-page-loaded JS code here; or even better, call 
	// 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
	//   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});


function listUsers(jsonArray, type){
	switch(type){
		case "updateMessages":
			var node = document.getElementById("M");
			while (node.firstChild) {
				node.removeChild(node.lastChild);
			}
			jsonArray.forEach(e => appendChild(node,e,type));
			break;
		case "updateUsersEvent":
			var node = document.getElementById("contUsers");
			while (node.firstChild) {
				node.removeChild(node.lastChild);
			}
			jsonArray.forEach(e => appendChild(node,e,type));
			var elements = document.getElementsByClassName("anUser");
			for (i = 0; i < elements.length;i++){
				elements[i].addEventListener("click",function() {
					go(config.rootUrl + "event/valorations/"+this.dataset.id,"POST",null).then(e => listUsers(e,"updateListValuations"));
				});
			}

			break;
		case "updateListValuations":
			var node = document.getElementById("contEvaluations");
			while (node.firstChild) {
				node.removeChild(node.lastChild);
			}
			jsonArray.forEach(e => appendChild(node,e,type));
			break;
	}
}


function appendChild(where,element, type){

	let html;
	switch(type){
		case "updateListValuations":
			html = ["<div class='anUser' data-id='"+element.id+"'>" +
				"<span>"+ element.evaluator +" - "+element.score+" - "+element.review+"</span>" + 
				"</div>"];
			break;
		case "updateUsersEvent":
			html = ["<div class='anUser' data-id='"+element.id+"'>" +
				"<span>"+ element.username +" - "+element.firstName+"</span>" + 
				"</div>"];
			break;
		case "updateMessages":
        		if (element.sender == "[[${session.u.id}]]" ) {
            			html = ["<div class='mensaje'>"+
				    	"<div class='mensajeMio'>"+
            				"<p> "+ element.textMessage +"</p>"+
            				"</div>"+
				    "</div>"];
		        } else {
            			html = ["<div class='mensaje'>"+
				    	"<div class='mensajeContacto'>"+
            				"<p> "+ element.textMessage +"</p>"+
            				"</div>"+
				    "</div>"];
			}
			break;

	}
	where.insertAdjacentHTML('beforeend',html);
}

