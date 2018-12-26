<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${ctx}/widgets/validation/jquery.validate.js"
	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/widgets/validation/cmxform.css" />
<script src="${ctx}/widgets/validation/jquery.metadata.js"
	type="text/javascript"></script>
<script src="${ctx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript"
	src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript"
	src="${ctx}/widgets/workflowEditor/swfobject.js"></script>

<script type="text/javascript">
	isUsingComonLayout = false;
	var hasInitHistory = false;
	$(document).ready(function() {
		$("#tabs").tabs({
			show : function(event, ui) {
				if (ui.index == 1 && !hasInitHistory) {
					changeViewSet("history");
					hasInitHistory = true;
				}
			}
		});
		formValidate();
		setTimeout(function() {
			$("#message").html("");
		}, 3000);
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
		$("#verifyDate").datepicker();
		$("#directorOpininDate").datepicker();
		$("#discoverDate").datepicker();
		$("#qualitySignDate").datepicker();
		$("input[name='disposeMethod']").each(function(index,obj){
			if($("#"+obj.id).next().is("label")){
				$("#"+obj.id).next().after("<br>");
			}else{
				$("#"+obj.id).next().next().after("<br>");
			}
		});
	});

	function formValidate() {
		addFormValidate('${fieldPermission}', 'form');
		var fieldPermission = ${fieldPermission};
		for ( var i = 0; i < fieldPermission.length; i++) {
			var obj = fieldPermission[i];
			if (obj.readonly == 'true') {
				$("#" + obj.name).attr("disabled", "disabled");
				$("#" + obj.name + "a").removeAttr("onclick");
				if(obj.name=="qualityState"){
					$(".aa","#form").parent().attr("style","display:none");
				}
				if(obj.name=="verifyState"){
					$(".aaa","#form").parent().attr("style","display:none");
				}
				 if(obj.name=="attachmentFiles"){
					$("#attachmentFiles","#form").parent().attr("style","display:none");
				} 
				$("select", "#form").each(function(index, obj) {
					$(obj).attr("disabled", "disabled");
				});
			}
			if (obj.controlType == "allReadolny") {
				$("input", "#form").each(function(index, obj) {
					$(obj).attr("disabled", "disabled");
				});
				$("textarea", "#form").each(function(index, obj) {
					$(obj).attr("disabled", "disabled");
				});
				$(".small-button-bg", "#form").each(function(index, obj) {
					$(obj).removeAttr("onclick");
				});
				$("button", "#form").each(function(index, obj) {
					$(obj).removeAttr("onclick");
				});
			}
		}
	}  	
	
	function chooseOne(cb) {   
        //先取得Div元素   
        var obj = document.getElementById("cbTd");   
        ///判斷obj中的子元素i是否為cb，若否則表示未被點選   
        for (var i=0; i<obj.children.length; i++){   
            if (obj.children[i]!=cb){
            	obj.children[i].checked = false;
            }else{
	            //若是 但原先未被勾選 則變成勾選；反之 則變為未勾選   
	            obj.children[i].checked = cb.checked;  
            } 
        }   
    }
	
 	function chooseOne1(cb) {   
		//先取得Div元素   
		var obj = document.getElementById("cbTd1");   
		//判斷obj中的子元素i是否為cb，若否則表示未被點選   
		for (var i=0; i<obj.children.length; i++){   
			if (obj.children[i]!=cb){
				obj.children[i].checked = false;
			}else{
	            //若是 但原先未被勾選 則變成勾選；反之 則變為未勾選   
	            obj.children[i].checked = cb.checked;  
			} 
		}   
	}
 	
	function chooseOne2(cb) {   
		//先取得Div元素   
		var obj = document.getElementById("cbTd2");   
		//判斷obj中的子元素i是否為cb，若否則表示未被點選   
     	for (var i=0; i<obj.children.length; i++){   
	        if (obj.children[i]!=cb){
	        	obj.children[i].checked = false;
	        }else{
	            //若是 但原先未被勾選 則變成勾選；反之 則變為未勾選   
	            obj.children[i].checked = cb.checked;  
			} 
		}   
	}
	
	//流转历史和表单信息切换
	function changeViewSet(opt) {
		if (opt == "history") {
			$("#tabs-2").load(
				"${iqcctx}/defective-goods/history.htm?taskId=${taskId}",
				function() {
					$("#tabs-2").height($(window).height() - 115);
				}
			);
		}
	}

	function validateForm() {
		addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
	}
	
	//表单验证  //aa.submit("defectiveGoodsForm", "", 'main', showMsg);
	function defectiveGoodsFormValidate() {
		$("#defectiveGoodsForm")
				.validate(
						{
							submitHandler : function() {
								$("#message").html("正在执行操作,请稍候... ...");
								$(".opt_btn").find("button.btn").attr(
										"disabled", "disabled");
								$("#defectiveGoodsForm")
										.ajaxSubmit(
												function(id) {
													$("#message").html("");
													window.location = "${mfgctx}/defective-goods/ledger/process-task.htm?taskId="
															+ $("#taskId")
																	.val();
												});
							}
						});
	}
	
	function selectPerson1(obj) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '选择人员',
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'false',
			hiddenInputId : obj.id,
			showInputId : obj.id,
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
				copyPersonLoginName();
			}
		});
	}
	
	function copyPersonLoginName() {
		$('#analyseLoginName').attr("value", jstree.getLoginName());
		$("#technologyDeptPrincipal").attr("value",jstree.getName());
	}
	
	function selectPerson2(obj) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '选择人员',
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'true',
			hiddenInputId : obj.id,
			showInputId : obj.id,
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
				copyPersonLoginName2();
			}
		});
	}
	function copyPersonLoginName2() {
		$('#reviewMenLoginNames').attr("value", jstree.getLoginNames());
	}
	
	function customSelectUser(title,showInputId,hiddenInputId,treeValue){
		var acsSystemUrl = "${ctx}";
		popTree({ title : title,
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:treeValue?treeValue:'loginName',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:hiddenInputId,
			showInputId:showInputId,
			acsSystemUrl:acsSystemUrl,
			callBack : function() {}
		});
	}
	
	<%-- 流程操作 --%>
	//提交form
	function completeTask(taskTransact) {
		if ($("#form").valid()) {
			$('#taskTransact').val(taskTransact);
			$("#form").attr("action",
					"${iqcctx}/defective-goods/complete-task.htm");
			$("#message").html("正在提交，请稍等... ...");
			$('#form').submit();

		} else {
			var error = $("#form").validate().errorList[0];
			$(error.element).focus();
			//$("#message").html(error.message);
			setTimeout(function() {
				$("#message").html("");
			}, 3000);

		}
		//window.location.href="${jianglingctx}/manage/process-task.htm?taskId=" + $("#taskId").attr("value") ;
	}
	
	//保存form
	function saveTask() {
		 /* getDetailItems();
		getComposingItems();
		$("#form").attr("action", "${iqcctx}/defective-goods/save.htm");
		$('#form').submit();  */
		var url = '${iqcctx}/defective-goods/save.htm';
		submitForm(url);
	}
	
	//办理完任务关闭窗口前执行
	function beforeCloseWindow(opt) {
		aa.submit("defaultForm1",
				"${mfgctx}/defective-goods/ledger/process-task.htm",
				'btnZone,viewZone');
	}

	//下载文档
	function downloadDoc(id) {
		window.open(webRoot + "/handle-significant/download-docment.htm?id="
				+ id);
	}

	//选择加签人员
	function addTask() {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '选择加签人员',
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'true',
			hiddenInputId : "addSignPerson",
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
				addSignCallBack();
			}
		});
	}
	
	function addSignCallBack() {
		$('#addSignPerson').attr("value", jstree.getLoginNames());
		$("#form").attr("action",
				"${iqcctx}/defective-goods/add-sign.htm");
		$("#form").ajaxSubmit(function(id) {
			alert(id);
		});
		validateForm();
	}
	
	//选择减签人员
	function cutTask() {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '选择加签人员',
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'true',
			hiddenInputId : "cutSignPerson",
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
				removeSignCallBack();
			}
		});
	}
	
	function removeSignCallBack() {
		$('#cutSignPerson').attr("value", jstree.getLoginNames());
		$("#form").attr("action",
				"${iqcctx}/defective-goods/remove-sign.htm");
		$("#form").ajaxSubmit(function(id) {
			alert(id);
		});
		validateForm();
	}
	
	//领取回调retriveTask
	function receiveback() {
		window.parent.close();
// 		$("#message").show("show");
// 		setTimeout('$("#message").hide("show");', 3000);
// 		$("#tabs").tabs();
// 		formValidate();
	}

	//上传后走
	function rewriteMethod() {
		ajaxSubmit("afterSalesAuditingOpinionForm", webRoot
				+ "/handle-significant/process-task.htm", "main", uploadBack);
	}

	function firstCallBack() {
		$('#firstLoginName').attr("value", getLoginName());
	}
	//保存信息
	function submitForm(url){
		 if($('#form').valid()){
			var params = getParams();
			$("#btnDiv").find("button.btn").attr("disabled",true);
			$("#message").html("正在保存,请稍候... ...");
			var saveUrl="${iqcctx}/defective-goods/save.htm";
			
			$.post(saveUrl,params,function(result){
				$("#btnDiv").find("button.btn").attr("disabled",false);
				$("#message").html("");
				if(result.error&&result.error!=""){
					alert(result.message);
				}else{
					$("#id").val(result.id);
					$("#message").html(result.message);
					showMsg();
				}
			},"json");
		} 
	}
	
	function getParams(){
		var params = {};
		$(":input","form").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name){
				if(obj.type == 'radio'){
					if(obj.checked){
						params[obj.name] = jObj.val();
					}else if(!params[obj.name]){
						params[obj.name] = '';
					}
				}else if(obj.type == 'checkbox'){
					if(obj.checked){
						if(!params[obj.name]){
							params[obj.name] = jObj.val();
						}else{
							params[obj.name] = params[obj.name] + "," + jObj.val();
						}
					}else{
						if(!params[obj.name]){
							params[obj.name] = '';
						}
					}
				}else{
					params[obj.name] = jObj.val();
				}
			}
		});
		return params;
	}
	
	function selectObj(title,id,treeType){
		var acsSystemUrl = "${ctx}";
		popTree({ title :title,
			innerWidth:'400',
			treeType:treeType,
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:id,
			showInputId:id,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}

	//提交
	workflowButtonGroup.btnSubmitTask.click = function(taskId) {
		completeTask('SUBMIT');
	};
	
	//同意
	workflowButtonGroup.btnApproveTask.click = function(taskId) {
		completeTask('APPROVE');
	};
	
	//不同意
	workflowButtonGroup.btnRefuseTask.click = function(taskId) {
		completeTask('REFUSE');
	};
	
	//加签
	workflowButtonGroup.btnAddCountersign.click = function(taskId) {
		addTask();
	};
	
	//减签
	workflowButtonGroup.btnDeleteCountersign.click = function(taskId) {
		cutTask();
	};

	//保存
	workflowButtonGroup.btnSaveForm.click = function(taskId) {
		saveTask();
	};

	//取回
	workflowButtonGroup.btnGetBackTask.click = function(taskId) {
		$.post("${iqcctx}/defective-goods/retrieve.htm?taskId=${taskId}", {},
				function(str) {
					alert(str);
					$("#btnBack").attr("disabled", "disabled");
					changeViewSet('basic');
					window.parent.close();
				});
	};

	//领取
	workflowButtonGroup.btnDrawTask.click = function(taskId) {
		aa.submit("form",
				"${iqcctx}/defective-goods/receive-task.htm", 'main', receiveback);
	};

	//指派
	workflowButtonGroup.btnAssign.click = function(taskId) {
		$.colorbox({
			href : "${iqcctx}/defective-goods/assign-tree.htm" + "?taskId="
					+ $("#taskId").attr("value") + "&id="
					+ $("#id").attr("value"),
			iframe : true,
			innerWidth : 400,
			innerHeight : 500,
			overlayClose : false,
			title : "指派人员"
		});
	};

	//已阅
	workflowButtonGroup.btnReadTask.click = function(taskId) {
		$('#taskTransact').val('READED');
		aa.submit("form",
				"${iqcctx}/manage/complete-task.htm", 'main',
				readTaskCallback);
	};

	//选择环节
	workflowButtonGroup.btnChoiceTache.click = function() {
		completeTask('READED');
	};

	function readTaskCallback() {
		$("#message").show("show");
		setTimeout('$("#message").hide("show");', 3000);
		window.parent.close();
	}

	//抄送
	workflowButtonGroup.btnCopyTache.click = function(taskId) {
		var acsSystemUrl = "${ctx}";
		popTree({
			title : '抄送人员',
			innerWidth : '400',
			treeType : 'MAN_DEPARTMENT_TREE',
			defaultTreeValue : 'id',
			leafPage : 'false',
			multiple : 'true',
			hiddenInputId : "assignee",
			showInputId : "assignee",
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
				copyPersonCallBack();
			}
		});
	};
	
	function copyPersonCallBack() {
		$('#assignee').attr("value", jstree.getLoginNames());
		$("#form").attr("action", "${iqcctx}/defective-goods/copy-tasks.htm");
		$("#form").ajaxSubmit(function(id) {
			alert(id);
		});
	}
	function endInstance(id){
 		var id = '${id}';
 		if(id==""){
 			alert("流程未提交");
 			return;
 		}	
 		if(confirm("确定要结束流程吗？")){
			$.post("${iqcctx}/defective-goods/end-instance.htm?id="+id, {}, function(result) {
				alert("流程结束");
				window.parent.close();
			});
		}
 	}
	/*
	//抄送
	function copyTache(){
		var taskId=$("#taskId").val();
		var acsSystemUrl = "${ctx}";
		popTree({ title :'抄送人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'true',
			hiddenInputId:"assignee",
			showInputId:"assignee",
			acsSystemUrl:acsSystemUrl,
			callBack:function(){copyPersonCallBack();}});
	}

	//取回
	
		function manageRetrieve(taskId) {
		$.post("${jianglingctx}/manage/retrieve.htm?taskId=${taskId}", {},
				function(str) {
					alert(str);
					$("#btnBack").attr("disabled", "disabled");
					changeViewSet('basic');
					window.parent.close();
				});
	};

	
	
	//任务指派
	function assign(){
		
	     $.colorbox({
			href : "${jianglingctx}/manage/assign-tree.htm?taskId="
					+ $("#taskId").attr("value") + "&id="
					+ $("#id").attr("value"),
			iframe : true,
			innerWidth : 400,
			innerHeight : 500,
			overlayClose : false,
			title : "指派人员"
		});
	}*/
	//流程的操作
</script>
</head>

<body>
	<div class="opt-body">
		<aa:zone name="main">
			<aa:zone name="btnZone">
				<div class="opt-btn">
					<div style="float: left;" id="btnDiv">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</div>
					<div style="float:left;padding-right:4px;line-height:28px;">
						<%
							List<Opinion> opinionParameters = (List<Opinion>)request.getAttribute("opinionParameters");
				    		if(opinionParameters!=null && opinionParameters.size()>0){
				    			Opinion param = opinionParameters.get(opinionParameters.size()-1);
				    	%>
				    	<%=DateUtil.formateTimeStr(param.getAddOpinionDate()) %>&nbsp;上一环节办理人：&nbsp;<%=param.getTransactorName() %>&nbsp;&nbsp;办理任务：&nbsp;<%=param.getTaskName() %>&nbsp;<%=param.getCustomField()==null?"":param.getCustomField() %>&nbsp;
						<%    	
					    	}
						%>
						<s:if test="task.active!=2">
							<a href="#opinion-table" style="text-decoration:underline;">填写意见</a>
						</s:if>
						<a href="#history-table" style="text-decoration:underline;">查看所有处理意见</a>
					</div>
				</div>
				<span id="message" style="color: red;"> <s:actionmessage theme="mytheme" /></span>
			</aa:zone>
			<div id="opt-content" class="form-bg">
				<form id="defaultForm1" name="defaultForm1" action="">
					<div id="tabs">
						<ul>
							<li><a href="#tabs-1">表单信息</a></li>
							<li><a href="#tabs-2">流转历史</a></li>
						</ul>
				</form>
				<div id="tabs-1" style="background: #ECF7FB;">
					<form method="post" id="form" action="">
						<div>
							<input type="hidden" name="taskId" id="taskId" value="${taskId}"/>
							<input type="hidden" name="assignee" id="assignee"/> 
							<input type="hidden" name="taskTransact" id="taskTransact"/> 
							<input type="hidden" name="launchState" id="launchState" value="${launchState}"/>
							<input type="hidden" name="addSignPerson" id="addSignPerson"/> 
							<input type="hidden" name="cutSignPerson" id="cutSignPerson"/>
							<input type="hidden" name="saveTaskFlag" id="saveTaskFlag"/>
							<input type="hidden" name="iteminfostr" id="iteminfostr"/>
						</div>
						<jsp:include page="form.jsp" />
						<%@ include file="process-form.jsp"%>
					</form>
				</div>
				<div id="tabs-2"></div>
			</div>
		</aa:zone>
	</div>
</body>
</html>