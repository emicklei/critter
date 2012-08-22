function deleteRule(name) {
	var answer = confirm('Delete rule?')
	if (answer) {
		$.ajax({
		  type: 'DELETE',
		  url: "/rules/"+name,
		  success: deleteRule_ok
		});
	}
}
function deleteRule_ok(data,textStatus,jqXHR) {
	window.location.href = "/";
}
function changeEnablementRule(name,enable) {
	var action = enable ? "enable" : "disable";
	$.ajax({
		  type: 'POST',
		  url: "/rules/"+name+"/"+action,
		  success: changeEnablementRule_ok
		});
}
function changeEnablementRule_ok(data,textStatus,jqXHR) {
	location.reload();
}

function clickedMethod(method) {
	if ('get' == method) {
		$('.get-post').show();
		$('.post-put').hide();
	}
	if ('post' == method) {
		$('.get-post').show();
		$('.post-put').show();
	}	
	if ('put' == method) {
		$('.get-post').hide();
		$('.post-put').show();
	}	
	if ('delete' == method) {
		$('.get-post').hide();
		$('.post-put').hide();
	}	
}