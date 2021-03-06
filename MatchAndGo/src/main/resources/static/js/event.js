
/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {
	var pageURL = window.location.href;
	var lastURLSegment = pageURL.substr(pageURL.lastIndexOf('/') + 1);
	if (config.socketUrl) {
		let subs = config.admin ? 
			["/topic/admin", "/user/queue/updates","/topic/event/"+lastURLSegment] : ["/topic/event/"+lastURLSegment]
		ws.initialize(config.socketUrl, subs);
	}
	if (!config.guest){
		if(config.canEvaluate){
			document.getElementById("sendValuation").addEventListener("click",function() {
				const id = document.getElementById("idUs").value;
				const score = document.getElementById("score").value;
				const text = document.getElementById("textoV").value;
				if (validateStrings(document.getElementById("contentVal"))){
					go(config.rootUrl + "reviews/add","POST",{textN:text,idU:id,score:score}).then(e => listUsers(e,"updateListValuations"));
				}
			});
		}
		document.getElementById("sendMessage").addEventListener("click",function() {
			const text = document.getElementById("texto").value;
			if (validateStrings(document.getElementById("contentMess"))){
				go(config.rootUrl + "event/nm/"+lastURLSegment,"POST",{textMessage:text,idU:config.userId}).then(e => listUsers(e,"updateMessages"));
			}
		});
	}
	go(config.rootUrl + "event/m/"+lastURLSegment,"POST",null).then(e => listUsers(e,"updateMessages"));
	go(config.rootUrl + "user/event/"+lastURLSegment,"POST",null).then(e => listUsers(e,"updateUsersEvent"));
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
					if (!config.guest && config.canEvaluate)
						document.getElementById("idUs").value = this.dataset.id;
					go(config.rootUrl + "reviews/user/"+this.dataset.id,"POST",null).then(e => listUsers(e,"updateListValuations"));
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
		case "pleaseExit":
			alert("Sorry but this page just deleted u must go other");
			window.location.href = "/event/";
			break;
		case "sayGoodBye":
			alert("admin say u goodbye :(");
			window.location.href = "/user/logout";
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
			if (element.sender == config.username ) {
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

