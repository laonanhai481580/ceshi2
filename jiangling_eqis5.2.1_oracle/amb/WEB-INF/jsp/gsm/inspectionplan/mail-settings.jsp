<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript">
	var selFlag = '<%=request.getParameter("selFlag")%>';
	$(document).ready(function(){
		$.spin.imageBasePath='${ctx}/widgets/spin/img/spin1/';
		$("#number1").spin({
			max:100,
			min:1
		});
		//初始化值
		initValue();
		
		$("#contentForm").validate({});
		//绑定是否启用到期检定提醒
		$(":input[name=delayRemindSwitch]").click(function(){
			changeDelayRemindSwitch();
		});
		changeDelayRemindSwitch();
		
		//绑定是否启用提前检定提醒
		$(":input[name=advanceRemindSwitch]").click(function(){
			changeAdvanceRemindSwitch();
		});
		changeAdvanceRemindSwitch();
		ruleId = $(":input[name=id]").val();
		createPersonGrid();
		
	});
	function initValue(){
		var id = '';
		if(selFlag=='select'){
			id = window.parent.$("#dynamicMeasurementInspectionPlan").jqGrid("getGridParam","selarrrow")[0];
		}else{
			id = window.parent.$("#dynamicMeasurementInspectionPlan").jqGrid("getDataIDs")[0];
		}
		var obj = window.parent.$("#dynamicMeasurementInspectionPlan").jqGrid("getRowData",id);
		$("#id").val(id);
		var params = {};
		for(var pro in obj){
			if(pro.indexOf("gsmMailSetting")==0){
				params[pro.split(".")[1]] = obj[pro];
			}
		}
		if(!params.advanceRemindSwitch||'true' != params.advanceRemindSwitch){
			$(":input[name=advanceRemindSwitch]").attr("checked","");
		}else{
			$(":input[name=advanceRemindSwitch]").attr("checked","checked");
			
			$obj = $(":input[name=advanceRemindTimeType]").attr("checked","checked");
			$obj.closest("tr").find(":input[type=text]").val(params.advanceRemindTime);
		}
		
		if(!params.delayRemindSwitch||'true' != params.delayRemindSwitch){
			$(":input[name=delayRemindSwitch]").attr("checked","");
		}else{
			$(":input[name=delayRemindSwitch]").attr("checked","checked");
			
			$obj = $(":input[name=delayRemindTimeType]").attr("checked","checked");
			$obj.closest("tr").find(":input[type=text]").val(params.delayRemindTime);
		}
	}
	//提前检定提醒
	function changeAdvanceRemindSwitch(){
		var checked = $(":input[name=advanceRemindSwitch]").attr("checked");
		if(checked){
			$("#advanceRemindTable").find(":input").attr("disabled","");
		}else{
			$("#advanceRemindTable").find(":input").attr("disabled","disabled");
		}
	}
	//到期未检定提醒
	function changeDelayRemindSwitch(){
		var checked = $(":input[name=delayRemindSwitch]").attr("checked");
		if(checked){
			$("#delayRemindTable").find(":input").attr("disabled","");
		}else{
			$("#delayRemindTable").find(":input").attr("disabled","disabled");
		}
	}
	
	function submitForm(url){
		
		if($("#contentForm").valid()){
			var params = {};
			
			var rows = jQuery("#person_table").jqGrid('getRowData');
			var paras = new Array();
			if(rows.length<1){
				alert("提醒人员不能为空！");
				return;
			}
			for(var i=0;i<rows.length;i++){
			    var row = rows[i];
			    paras.push('{"name":"'+row.name+'","userId":"'+row.userId+'"}');
			}
			params.personStrs = "[" + paras.toString() + "]";
			
			var advanceChecked = $(":input[name=advanceRemindSwitch]").attr("checked");
			var delayChecked = $(":input[name=delayRemindSwitch]").attr("checked");
			params.advanceRemindSwitch=advanceChecked;
			params.delayRemindSwitch=delayChecked;
			
			if(advanceChecked){
				params.advanceRemindTime = $(":input[name=advanceRemindTime]").val();
			}
			if(delayChecked){
				params.delayRemindTime = $(":input[name=delayRemindTime]").val();
			}
				
			params.selFlag = selFlag;
			
			
			window.parent.getIndicators(selFlag,params);
			$(".opt-btn .btn").attr("disabled","disabled");
			$("#message").html("正在保存配置,请稍候... ...");
			$.post(url,params,function(result){
				$(".opt-btn .btn").attr("disabled","");
				$("#message").html(result.message);
				if(result.error){
					alert(result.message);
				}else{
					window.parent.$.colorbox.close();
				}
			},'json');
		}
	}
	
	//选择提醒人员
	function addPerson(){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择提醒人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'true',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setPerson();
			}
		});
	}
	
	function setPerson(){
		var personIds = $("#personId").val().split(",");
		var personName = $("#personName").val().split(",");
		var rows = jQuery("#person_table").jqGrid('getRowData'); 
		var mydata = [];
		var isExit;
		for(var i=0;i<personIds.length;i++){
			for(var y=0;y<rows.length;y++){
				if(personIds[i]==rows[y].userId){
					isExit = true;
					break;
				}
			}
			if(!isExit){
				mydata.push({userName:personName[i],userLoginName:personIds[i],targetId:personIds[i]});
			}
			isExit = false;
		}
		for(var i=0;i<=mydata.length;i++){
			jQuery("#person_table").jqGrid('addRowData',i+1,mydata[i]);
		}
	}
	
	//删除提醒人员
	function delPerson() {
		var rows = jQuery("#person_table").getGridParam('selarrrow');
		if(rows.length==0){
			alert("请选择所要删除的人员！");
			return;
		}
		rows = rows.join(",").split(",");
		for(var i=0;i<rows.length;i++){
			jQuery("#person_table").jqGrid("delRowData",rows[i]);
		}
	}
	
	function createPersonGrid(){
		$("#person_table").jqGrid({
			url : '${gsmctx}/inspectionplan/warn-user-datas.htm',
			postData : {id:ruleId},
			height : 100,
			width : 500,
			datatype : "json",
			rownumbers : true,
			colNames : ${personGridColumnInfo.colNames},
			colModel : ${personGridColumnInfo.colModel},
			forceFit : true,
		   	shrinkToFit : true,
		   	autowidth : false,
			viewrecords : true, 
			sortorder : "desc",
			gridComplete : function(){}
		});
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<aa:zone name = "main">
				<div class="opt-btn">
				<button class='btn' onclick="submitForm('${gsmctx}/inspectionplan/save-mail-settings.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button  class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<span id="message" style="color:red;"></span>
				</div>
				<div style="margin-top:15px; margin-left: 10px;margin-right:10px">
					<form id="contentForm" name="contentForm" method="post" >
					<input type="hidden" id="id" name="id"></input>
					<fieldSet>
						<legend>&nbsp;&nbsp;<input type="checkbox" name="advanceRemindSwitch" id="advanceRemindSwitch"><label for="advanceRemindSwitch">启用提前检定提醒&nbsp;&nbsp;<label/></legend>
						<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="advanceRemindTable">
				    		<tr>
				    			<td style="width:160px;">
				    				<input type="radio" name="advanceRemindTimeType" value="fixed" checked="checked"/>提前
				    			</td>
				    			<td><input type="text"  style="width:60px;" name="advanceRemindTime" value="0.5" class="{required:true,number:true,min:0,messages:{number:'必须为数字格式!',min:'不能为负数!'}}"/>&nbsp;小时检定提醒.</td>
				    		</tr>
				    		<tr>
				    			<td></td>
				    			<td>
				    				数字类型,小于一个小时用小数表示,如0.5表示超过半个小时提醒.
				    			</td>
				    		</tr>
				    	</table>
					</fieldSet>
					&nbsp;&nbsp;
					<fieldSet style="margin-top:6px;">
						<legend>&nbsp;&nbsp;<input type="checkbox" name="delayRemindSwitch" id="delayRemindSwitch"><label for="delayRemindSwitch">启用到期未检定提醒&nbsp;&nbsp;<label/></legend>
						<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="delayRemindTable">
				    		<tr>
				    			<td style="width:160px;">
				    				<input type="radio" name="delayRemindTimeType" value="fixed" checked="checked"/>超期
				    			</td>
				    			<td><input type="text"  style="width:60px;" name="delayRemindTime" value="0.5" class="{required:true,number:true,min:0,messages:{number:'必须为数字格式!',min:'不能为负数!'}}"/>&nbsp;小时未检定提醒.</td>
				    		</tr>
				    		<tr>
				    			<td></td>
				    			<td>
				    				数字类型,小于一个小时用小数表示,如0.5表示超过半个小时提醒.
				    			</td>
				    		</tr>
				    	</table>
					</fieldSet>
					<fieldSet style="margin-top:6px;">
						<legend>&nbsp;&nbsp;<input type="checkbox" name="setPerson" id="setPerson"><label for="setPerson">设置提醒人员&nbsp;&nbsp;<label/></legend>
							<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="delayRemindTable">
				    		<tr>
								<td style="padding-bottom: 4px">
									<a class="small-button-bg" style="vertical-align: middle;" href="javascript:addPerson();" title="添加提醒人员"><span class="ui-icon ui-icon-plusthick" style='cursor:pointer;'></span></a>
									<a class="small-button-bg" style="vertical-align: middle;" href="#" onclick="delPerson();" title="删除提醒人员"><span class="ui-icon ui-icon-minusthick" style='cursor:pointer;'></span></a>
								</td>
							</tr>
							<tr>
								<td >
									<input id="personId" name="personId" type="hidden"/>
									<input id="personName" name="personName" type="hidden"/>
									<table id="person_table"></table>
								</td>
						</tr>
				    	</table>
					</fieldSet>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>