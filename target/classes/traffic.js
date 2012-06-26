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