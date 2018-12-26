<%@page import="com.norteksoft.wf.engine.entity.InstanceHistory"%>
<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/handlers.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/workflowEditor/swfobject.js"></script>
	<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<jsp:include page="form-script.jsp" />
	<script type="text/javascript">
		isUsingComonLayout = false;
		var hasInitHistory = false;
		$(document).ready(function() {
			$( "#tabs" ).tabs({
				show: function(event, ui) {
					if(ui.index==1&&!hasInitHistory){
						changeViewSet("history");
						hasInitHistory = true;
					}
				}
			});
			initForm();
			//意见
			$(":input[name=opinion]").removeAttr("disabled").removeAttr("readonly");
			//添加验证
			//addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
			
			setTimeout(function(){
				$("#opt-content").height($(window).height()-70);
			},500);
		});


		//流转历史和表单信息切换
		function changeViewSet(opt) {
			if (opt == "history") {
				$("#tabs-2").load("${mfgctx}/defective-goods/ledger/history.htm?taskId=${taskId}",function(){
					$("#tabs-2").height($(window).height()-115);
				});
			}
		}
		
		function validateForm(){
			addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
		}
		//表单验证  //aa.submit("defectiveGoodsForm", "", 'main', showMsg);
		function defectiveGoodsFormValidate() {
			$("#defectiveGoodsForm").validate({
				submitHandler : function() {
					$("#message").html("正在执行操作,请稍候... ...");
					$(".opt_btn").find("button.btn").attr("disabled", "disabled");
					$("#defectiveGoodsForm").ajaxSubmit(function(id) {
						$("#message").html("");
						window.location = "${mfgctx}/defective-goods/ledger/process-task.htm?taskId="+ $("#taskId").val();
					});
				}
			});
		}
		
		
		<%-- ---流程操作--- --%>
		
		//提交form
		function completeTask(taskTransact) {
			if(taskTransact != 'REFUSE'){
				if(!$("#defectiveGoodsForm").valid()){
					var error = $("#defectiveGoodsForm").validate().errorList[0];
					$(error.element).focus();
					showMessage("错误提示:" + error.message);
					return;
				}
			}
			if($("#launchState").val()=='0'){
	 			getDetailItems();	 			
	 		}
	 		getComposingItems();
			$('#taskTransact').val(taskTransact);
			$("#defectiveGoodsForm").attr("action",
					"${mfgctx}/defective-goods/ledger/complete-task.htm");
			$('#defectiveGoodsForm').submit();
		}
		//保存form
		function saveTask() {
			getDetailItems();
	 		getComposingItems();
			$("#defectiveGoodsForm").attr("action","${mfgctx}/defective-goods/ledger/save.htm?processTaskSave=true");
			$('#defectiveGoodsForm').submit();
		}
		//办理完任务关闭窗口前执行
		function beforeCloseWindow(opt) {
			aa.submit("defaultForm1", "${mfgctx}/defective-goods/ledger/process-task.htm", 'btnZone,viewZone');
		}

		//下载文档
		function downloadDoc(id) {
			window.open(webRoot + "/handle-significant/download-docment.htm?id=" + id);
		}

		//选择加签人员
		function addTask() {
			var acsSystemUrl = "${marketctx}";
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
			$('#addSignPerson').attr("value", getLoginNames());
			$("#defectiveGoodsForm").attr("action",
					"${mfgctx}/defective-goods/ledger/add-sign.htm");
			$("#defectiveGoodsForm").ajaxSubmit(function(id) {
				alert(id);
			});
			validateForm();
		}
		//选择减签人员
		function cutTask() {
			$.colorbox({
				href : webRoot + "/handle-significant/cutsign.htm" + "?taskId="	+ $("#taskId").attr("value") + "&id=" + $("#id").attr("value"),
				iframe : true,
				innerWidth : 400,
				innerHeight : 500,
				overlayClose : false,
				title : "请选择减签人"
			});
		}

		//领取回调
		function receiveback() {
			$("#message").show("show");
			setTimeout('$("#message").hide("show");', 3000);
			$("#tabs").tabs();
			validateForm();
		}
		//上传后走
		function rewriteMethod() {
			ajaxSubmit("afterSalesAuditingOpinionForm", webRoot
					+ "/handle-significant/process-task.htm", "main", uploadBack);
		}

		function firstCallBack() {
			$('#firstLoginName').attr("value", getLoginName());
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
			$.post("${mfgctx}/defective-goods/ledger/retrieve.htm?taskId=${taskId}",{},function(str){
				alert(str);
				$("#btnBack").attr("disabled", "disabled");
				changeViewSet('basic');
				window.parent.close();
			});
		};

		//领取
		workflowButtonGroup.btnDrawTask.click = function(taskId) {
			aa.submit("defectiveGoodsForm",
					"${mfgctx}/defective-goods/ledger/receive-task.htm", 'main',
					receiveback);
		};

		//指派
		workflowButtonGroup.btnAssign.click = function(taskId) {
			$.colorbox({
				href : "${mfgctx}/defective-goods/ledger/assign-tree.htm"
						+ "?taskId=" + $("#taskId").attr("value") + "&id="
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
			aa.submit("defectiveGoodsForm",
					"${mfgctx}/defective-goods/ledger/complete-task.htm",
					'main', readTaskCallback);
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
			$('#assignee').attr("value",jstree.getLoginNames());
			$("#defectiveGoodsForm").attr("action",
					"${mfgctx}/defective-goods/ledger/copy-tasks.htm");
			$("#defectiveGoodsForm").ajaxSubmit(function(id) {
				alert(id);
			});
		}
		<%-- ---流程操作--- --%>
		/*---------------------------------------------------------
		函数名称:showIdentifiersDiv
		参          数:
		功          能:标识为（下拉选）
		------------------------------------------------------------*/
		function showIdentifiersDiv(){
			if($("#flag").css("display")=='none'){
				removeSearchBox();
				$("#flag").show();
				var position = $("#_task_button").position();
				$("#flag").css("left",position.left+15);
				$("#flag").css("top",position.top+28);
			}else{
				$("#flag").hide();
			}
		}
		
		var identifiersDiv;
		function hideIdentifiersDiv(){
			identifiersDiv = setTimeout('$("#flag").hide()',300);
		}
		
		function show_moveiIdentifiersDiv(){
			clearTimeout(identifiersDiv);
		}
		//驳回任务
	 	function backToTask(obj){
	 		hideIdentifiersDiv();
	 		var opinion = $(":input[name=opinion]").val();
	 		if(!opinion){
	 			showMessage("意见必填!");
	 			$(":input[name=opinion]").focus();
	 			return;
	 		}
	 		var returnTaskName = $(obj).attr("taskName");
	 		if(confirm("确定要驳回到【" + returnTaskName + "】吗?")){
	 			clearTimeout(window.msgTimeout);
	 			$("button").attr("disabled","disabled");
	 			$("#message").html("正在执行操作,请稍候... ...").show();
	 			var params = {taskId:'${taskId}',returnTaskName:returnTaskName,opinion:opinion};
	 			$.post("${mfgctx}/defective-goods/ledger/return-to-task.htm",params,function(result){
	 				$("button").removeAttr("disabled");
	 				$("#message").html("");
	 				if(result.error){
	 					alert(result.message);
	 				}else{
	 					if(window.parent){
	 						window.parent.close();
	 					}else{
	 						window.location.href = '${mfgctx}/defective-goods/ledger/process-task.htm?taskId=${taskId}';
	 					}
	 				}
	 			},'json');
	 		};
	 	}
	 	
	 	function goToAuditIqc(iqcId){
	 		$("#message").html("正在转到审核页面,请稍候... ...").show();
	 		$("button").attr("disabled","disabled");
	 		window.location.href = '${iqcctx}/inspection-report/input.htm?sourceTaskId=${taskId}&id=' + iqcId;
	 	}
	</script>
</head>

<body
	onclick="$('#sysTableDiv').hide();
        $('#styleList').hide();">
	<div class="opt-body">
		<aa:zone name="main">
			<aa:zone name="btnZone">
				<div class="opt-btn">
					<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					<s:if
						test="%{task.active==0&&#auditIqcId>0}">
						<button  class='btn' type="button" onclick="goToAuditIqc(${auditIqcId})"><span><span><b class="btn-icons btn-icons-audit"></b>审核进货检验单</span></span></button>
					</s:if>
					<s:if
						test="task.active==0&&returnableTaskNames.size>0">
						<button  class='btn' type="button" id="_task_button" onclick="showIdentifiersDiv();"><span><span><b class="btn-icons btn-icons-back"></b>驳回到</span></span></button>
					</s:if>	
				</div>
				<span id="message" style="color:red;">
					<s:actionmessage theme="mytheme" />
				</span>
			</aa:zone>
			<div id="opt-content" class="form-bg">
				<div id="flag" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
					<ul style="width:300px;">
					 <s:iterator var="returnTaskName" value="returnableTaskNames">
					 <li onclick="backToTask(this);" style="cursor:pointer;" title="驳回到 ${returnTaskName}" taskName="${returnTaskName}">
					  ${returnTaskName}
					 </li>
					 </s:iterator>
					</ul>
				</div>
				<form id="defaultForm1" name="defaultForm1" action="">
					<div>
						<input type="hidden" name="id" id="id" value="${id}" />
						<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
					</div>
				</form>
				<div id="tabs" >
					<ul>
						<li><a href="#tabs-1">表单信息</a>
						</li>
						<li><a href="#tabs-2">流转历史</a>
						</li>
					</ul>
					<div id="tabs-1" style="background:#ECF7FB;">
						<form  method="post" id="defectiveGoodsForm" action="" >
							<div>
								<input type="hidden" name="id" id="id" value="${id }" />
								<input type="hidden" name="taskId" id="taskId" value="${taskId}" />
								<input type="hidden" name="assignee" id="assignee" />
								<input type="hidden" name="taskTransact" id="taskTransact" />
								<input type="hidden" name="launchState" id="launchState" value="${launchState}"/>
							</div>
							<jsp:include page="form.jsp" />
						<%
							List<InstanceHistory> instanceHistories = (List<InstanceHistory>)request.getAttribute("instanceHistories");
							if(instanceHistories==null){
								instanceHistories = new ArrayList<InstanceHistory>();
							}
						%>
							<table class="form-table-border-left" id="history-table" style="width:100%;margin: auto;">
								<tr height=28>
									<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:5%;text-align: center">
										序号
									</td>
									<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:15%;text-align: center">
										环节名称
									</td>
									<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:35%;text-align: center">
										流程操作
									</td>
									<td style="background:#99CCFF;font-weight: bold;font-size:14px;width:45%;text-align: center">
										办理意见
									</td>
								</tr>
								<%
									int index = 1;
									for(InstanceHistory param : instanceHistories){
										String taskName = param.getTaskName();
										if(taskName == null){
											if(param.getTransactionResult() != null){
												if(param.getTransactionResult().endsWith("流程启动")){
													taskName = "流程开始";
												}else if(param.getTransactionResult().endsWith("流程结束")){
													taskName = "流程结束";
												}
											}
										}
										String transactorOpinion = param.getTransactorOpinion();
										if("流程开始".equals(taskName)||"流程结束".equals(taskName)){
											transactorOpinion = "";
										}
								%>
									<tr height=24>
										<td style="text-align:center">
											<%=index++ %>
										</td>
										<td>
											<%=taskName==null?"":taskName %>
										</td>
										<td>
											<%=param.getTransactionResult() %>
										</td>
										<td>
											<%=transactorOpinion==null?"":transactorOpinion %>
										</td>
									</tr>
								<%} %>
								<s:if test="task.active==0">
								<table class="form-table-border-left" id="opinion-table" style="width:100%;margin:auto;margin-top:4px;">
									<tr height=28>
										<td style="background:#99CCFF;font-weight: bold;font-size:14px;">
											我的意见
										</td>
									</tr>
									<tr height=28>
										<td>
											<textarea rows="6" name="opinion" style="width:99.5%;"></textarea>
										</td>
									</tr>
								</table>
							</s:if>
							</table>
						</form>
					</div>
					<div id="tabs-2">
					</div>
				</div>
			</div>
		</aa:zone>
	</div>
</body>
</html>