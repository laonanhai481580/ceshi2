<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.util.Map"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function initForm(){

		$(":input[isLogin]").bind("click",function(){
			selectObj("选择会签人",$("#"+this.id).prev().attr("name"),this.id,"loginName");
		});
/* 		$("input[name^='operationTime']").each(function(index,obj){
			$(obj).datepicker({changeMonth:true,changeYear:true});
		});
		 */
		//格式化附件
		var attachments = [		                   
		     {showInputId:'showRaFileAttach',hiddenInputId:'raFileAttach'},
		     {showInputId:'showXrfFileAttach',hiddenInputId:'xrfFileAttach'},
		     {showInputId:'showTryFileAttach',hiddenInputId:'tryFileAttach'},
		     {showInputId:'showHarmfulFileAttach',hiddenInputId:'harmfulFileAttach'},
		     {showInputId:'showQsSignFile',hiddenInputId:'qsSignFile'},
		];
		//上传附件
		parseDownloadFiles(attachments);
		$("tr[lanuch]").each(function(index,obj){
			if($(obj).attr("lanuch")=='${workCode}'){
				addFormValidate('${fieldPermission}', 'inputForm');
			}else{
				$(obj).find(":input").attr("disabled","disabled");
				$(obj).find("button").hide();
			}
		});
		$("a").each(function(index,obj){
			if($(obj).attr("lanuch")!='${workCode}'){
				$(obj).removeAttr("onclick");
			}
		});
		$("#flagIds").removeAttr("disabled");
		checkboxChange();
		<% Map<String, String> map=(Map<String, String>)ActionContext.getContext().get("signMan");   
		String loginName=ContextUtils.getLoginName();
		if(map.size()>0&&map.containsKey(loginName)){
		%>	
			$("tr[edit]").each(function(index,obj){
				if($(obj).attr("edit")!='<%=map.get(loginName)%>'){
					$(obj).find(":input").attr("disabled","disabled");
				}else{
					$(obj).find(":input").removeAttr("disabled");
				}
			});				
		<%
		}		
		%>			
	}
	var rowId=null;
	function itemNumberClick(obj){
		rowId=obj.id.split("_")[0];
		$.colorbox({href:"${iqcctx}/inspection-base/inspection-bom/bom-inspection-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
	}
	function setInspectionBomValue(datas){
		$("#"+rowId+"_beforeCode").val(datas[0].materielCode);
		$("#"+rowId+"_beforeName").val(datas[0].materielName);
 	}	
	function checkboxChange(){
		$("input[label=true]").each(function(index,obj){
			var check=$("#"+obj.id).attr("checked");
			if(check){
				$("#"+obj.id+"_label").attr("style","background-color:yellow");
			}else{
				$("#"+obj.id+"_label").removeAttr("style","background-color:yellow");
			}
		});		
	};	
	//物料清单弹出框选择
	function checkBomClick(){
		$.colorbox({href:"${mfgctx}/common/product-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setProductValue(datas){
		$("#machineNo").val(datas[0].materielCode);
 	}
	//物料清单弹出框选择
	var objIndex = "";
	function selectBomClick(obj){
		var objId = obj.id;
		objIndex = objId.split("_")[0];
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setFullBomValue(datas){
		$("#"+objIndex+"_beforeCode").val(datas[0].materielCode);
		$("#"+objIndex+"_beforeName").val(datas[0].materielName);
 	}
	function uploadFiles(showInputId,hiddenInputId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showInputId,
			hiddenInputId : hiddenInputId
		});
	}
	//选择检验人员
 	function selectObj(title,hiddenInputId,showInputId,treeValue){
		var acsSystemUrl = "${ctx}";
		popTree({ title : title,
			innerWidth:'400',
			treeType:"MAN_DEPARTMENT_TREE",
			defaultTreeValue:treeValue?treeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId :hiddenInputId,
			showInputId : showInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	function parseDownloadFiles(arrs){
		for(var i=0;i<arrs.length;i++){
			var hiddenInputId = arrs[i].hiddenInputId;
			var showInputId = arrs[i].showInputId;
			var files = $("#"+hiddenInputId).val();
			if(files&&files.length>0){
		 		$.parseDownloadPath({
					showInputId : showInputId,
					hiddenInputId : hiddenInputId
				});
			}
		}
	}
	function addRowHtml(obj){
		var tr = $(obj).closest("tr");
		var clonetr = tr.clone(false);
		tr.after(clonetr);
		var total = $("#fir");
		var num = total.val();
		clonetr.find(":input").each(function(index ,obj){
			obj=$(obj);
			var fieldName=obj.attr("fieldName");
			obj.attr("id","flag"+num+"_"+fieldName).val("");
			obj.attr("name","flag"+num+"_"+fieldName);
		});
		$("#fir").val(parseInt(num)+1);
		$("#flagIds").val($("#flagIds").val()+","+"flag"+num);
	}
	function removeRowHtml(obj){
		var tr = $(obj).parent().parent("td").parent("tr");
		var pre = tr.prev("tr").attr("name");
		var next = tr.next("tr").attr("name");
		if (next != undefined) {
			tr.remove();
		} else if (pre != undefined) {
			tr.remove();
		} else {
			alert('至少要保留一行');
		}
	}
	function getLoginStr(){
		var loginStr="";
		$(":input[login=true]").each(function(index,obj){
			if($(obj).val()!=""){
				loginStr+=$(obj).val()+",";
			}
		});
		$("#jointlySignStrs").val(loginStr);
	}
	function submitForm(url){
		if($("#inputForm").valid()){
			getLoginStr();
			if($("#jointlySignStrs").val()==""){
				alert("请选择会签人");
				return ;
			}
			$("tr[tableName=dcrnReportDetails]").find(":input").removeAttr("disabled");
			$('#inputForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inputForm').submit();
		}else{
			var error = $("#inputForm").validate().errorList[0];
			$(error.element).focus();
		}
	}
	//提交form
	function completeTask(taskTransact) {
		if(taskTransact != 'REFUSE'){
			if(!$("#inputForm").valid()){
				var error = $("#inputForm").validate().errorList[0];
				$(error.element).focus();
				showMessage("错误提示:" + error.message);
				return;
			}
		}
		$("tr[tableName=dcrnReportDetails]").find(":input").removeAttr("disabled");
		$('#taskTransact').val(taskTransact);
		$("#inputForm").attr("action",
				"${ecmctx}/dern/complete-task.htm");
		$('#inputForm').submit();
	}
	//保存form
	function saveTask() {
// 		$("#inputForm").attr("action","${ecmctx}/dern/save.htm?processTaskSave=true");
		submitForm("${ecmctx}/dern/save.htm?processTaskSave=true")
// 		$('#inputForm').submit();
	}
	//办理完任务关闭窗口前执行
	function beforeCloseWindow(opt) {
		aa.submit("defaultForm1", "${ecmctx}/dern/process-task.htm", 'btnZone,viewZone');
	}

	//下载文档
	function downloadDoc(id) {
		window.open(webRoot + "/handle-significant/download-docment.htm?id=" + id);
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
		$("#inputForm").attr("action",
				"${ecmctx}/dern/addsigner.htm");
		$("#inputForm").ajaxSubmit(function(id) {
			alert(id);
		});
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
			hiddenInputId : "removeSignPerson",
			acsSystemUrl : acsSystemUrl,
			callBack : function() {
				removeSignCallBack();
			}
		});
	}
	
	function removeSignCallBack() {
		$('#removeSignPerson').attr("value", jstree.getLoginNames());
		$("#form").attr("action",
				"${ecmctx}/dern/removesigner.htm");
		$("#form").ajaxSubmit(function(id) {
			alert(id);
		});
	}
	//领取回调
	function receiveback() {
		$("#message").show("show");
		setTimeout('$("#message").hide("show");', 3000);
		$("#tabs").tabs();
		validateForm();
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
		$.post("${ecmctx}/dern/retrievetask.htm?taskId=${taskId}",{},function(str){
			alert(str);
			$("#btnBack").attr("disabled", "disabled");
			changeViewSet('basic');
			window.parent.close();
		});
	};
	//流转历史和表单信息切换
	function changeViewSet(opt) {
		if (opt == "history") {
			$("#tabs-2").load("${ecmctx}/dern/show-history.htm?taskId=${taskId}",function(){
				$("#tabs-2").height($(window).height()-115);
			});
		}
	}
	//领取
	workflowButtonGroup.btnDrawTask.click = function(taskId) {
		aa.submit("inputForm",
				"${ecmctx}/dern/receive-task.htm", 'main',
				receiveback);
	};

	//指派
	workflowButtonGroup.btnAssign.click = function(taskId) {
		$.colorbox({
			href : "${ecmctx}/dern/assign-tree.htm"
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
		aa.submit("inputForm",
				"${ecmctx}/dern/complete-task.htm",
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
		$("#inputForm").attr("action",
				"${ecmctx}/dern/copy-tasks.htm");
		$("#inputForm").ajaxSubmit(function(id) {
			alert(id);
		});
	}
	function viewFormInfo(){
		var formId=$("#id").val();
		if(!formId){
			alert("初始化时未指定对象ID!");
			return;
		}
		$.colorbox({href:'${ecmctx}/dern/view-info.htm?id='+formId+"#tabs-2",iframe:true,
			innerWidth:$(window).width()<1100?$(window).width()-50:1100, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"DCR/N变更单详情"
		});
		
	}
</script>