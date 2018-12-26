//驳回
function showIdentifiersDiv() {
	if ($("#flag").css("display") == 'none') {
		removeSearchBox();
		$("#flag").show();
		var position = $("#_task_button").position();
		$("#flag").css("left", position.left + 15);
		$("#flag").css("top", position.top + 28);
	} else {
		$("#flag").hide();
	}
}
var identifiersDiv;
function hideIdentifiersDiv() {
	identifiersDiv = setTimeout('$("#flag").hide()', 300);
}

function show_moveiIdentifiersDiv() {
	clearTimeout(identifiersDiv);
}
// 驳回任务
function backToTask(obj, url, taskId) {
	hideIdentifiersDiv();
	var opinion = $("textarea[name=opinion]").val();
	if (!opinion) {
		alert("意见必填!");
		$("textarea[name=opinion]").focus();
		return;
	}
	var returnTaskName = $(obj).attr("taskName");
	if (confirm("确定要驳回到"+"【" + returnTaskName + "】吗?")) {
		clearTimeout(window.msgTimeout);
		$("button").attr("disabled", "disabled");
		$("#message").html("正在执行操作,请稍候... ...").show();
		var params = {
			taskId : taskId,
			returnTaskName : returnTaskName,
			opinion : opinion
		};
		$.post(url + "/return-to-task.htm", params, function(result) {
			$("button").removeAttr("disabled");
			$("#message").html("");
			if (result.error) {
				alert(result.message);
			} else {
				alert("驳回成功！");
				$(".opt-btn").find("button.btn").attr("disabled",true);
			};
		}, 'json');
	};
}