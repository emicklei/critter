function deleteRule(name) {
    var answer = confirm('Delete rule?')
    if (answer) {
        $.ajax({
            type: 'DELETE',
            url: "/rules/" + name,
            success: deleteRule_ok
        });
    }
}
function deleteRule_ok(data, textStatus, jqXHR) {
    window.location.href = "/";
}
function changeEnablementRule(name, enable) {
    var action = enable ? "enable" : "disable";
    $.ajax({
        type: 'POST',
        url: "/rules/" + name + "/" + action,
        success: changeEnablementRule_ok
    });
}
function changeEnablementRule_ok(data, textStatus, jqXHR) {
    window.location.reload();
}

function changeTracingRule(name, tracing) {
    var action = tracing ? "trace-on" : "trace-off";
    $.ajax({
        type: 'POST',
        url: "/rules/" + name + "/" + action,
        success: changeTracingRule_ok
    });
}

function changeTracingRule_ok(data, textStatus, jqXHR) {
    window.location.reload();
}

function clickedMethod(method) {
    if ('GET' == method) {
        $('.get-post').show();
        $('.post-put').hide();
    }
    if ('POST' == method) {
        $('.get-post').show();
        $('.post-put').show();
    }
    if ('PUT' == method) {
        $('.get-post').hide();
        $('.post-put').show();
    }
    if ('DELETE' == method) {
        $('.get-post').hide();
        $('.post-put').hide();
    }
}